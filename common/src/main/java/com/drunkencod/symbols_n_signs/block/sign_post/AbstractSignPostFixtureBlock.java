package com.drunkencod.symbols_n_signs.block.sign_post;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.item.IWrenchConfigurable;
import com.drunkencod.symbols_n_signs.item.RatchetWrenchItem;
import com.drunkencod.symbols_n_signs.platform.Services;
import com.drunkencod.symbols_n_signs.registry.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

/** Base class for all sign post fixture blocks. */
public abstract class AbstractSignPostFixtureBlock extends FaceAttachedHorizontalDirectionalBlock
        implements EntityBlock, SimpleWaterloggedBlock, IWrenchConfigurable {

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    protected static final Map<Direction, BooleanProperty> DIRECTION_PROPS = SignPostNetworkUtil
            .getDirectionProperties();

    public AbstractSignPostFixtureBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false).setValue(EAST, false)
                .setValue(SOUTH, false).setValue(WEST, false)
                .setValue(UP, false).setValue(DOWN, false)
                .setValue(WATERLOGGED, false)
                .setValue(POWERED, false));
    }

    // #region Block states

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder); // Block has no properties to add
        builder.add(FACE, FACING, NORTH, EAST, SOUTH, WEST, UP, DOWN, WATERLOGGED, POWERED);
    }

    /**
     * Returns the direction on the sign post this fixture occupies (the face from
     * which the fixture element protrudes outward). No post branch is shown on this
     * side.
     * Convention: FACE=WALL → FACING; FACE=FLOOR → DOWN; FACE=CEILING → UP.
     */
    public abstract Direction getFixtureDirection(BlockState state);

    // #region Placement & survival

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        // Fixtures replace the sign post block at the clicked position
        BlockPos pos = ctx.getClickedPos();
        if (!ctx.getLevel().getBlockState(pos).is(ModBlocks.SIGN_POST.get()))
            return null;

        LevelAccessor level = ctx.getLevel();
        boolean waterlogged = level.getFluidState(pos).getType() == Fluids.WATER;
        BlockState base = super.getStateForPlacement(ctx);
        if (base == null)
            return null;
        return setConnectionStates(base, level, pos).setValue(WATERLOGGED, waterlogged);
    }

    @Override
    protected BlockState updateShape(BlockState ownState, Direction direction, BlockState neighborState,
            LevelAccessor level, BlockPos ownPos, BlockPos neighborPos) {
        if (ownState.getValue(WATERLOGGED))
            level.scheduleTick(ownPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

        // Never connect toward the fixture element face
        if (direction == getFixtureDirection(ownState))
            return ownState.setValue(DIRECTION_PROPS.get(direction), false);

        boolean shouldConnect = SignPostNetworkUtil.shouldConnectTo(level, neighborState, neighborPos, direction);
        return ownState.setValue(DIRECTION_PROPS.get(direction), shouldConnect);
    }

    private BlockState setConnectionStates(BlockState state, LevelAccessor level, BlockPos pos) {
        Direction blocked = getFixtureDirection(state);
        for (Map.Entry<Direction, BooleanProperty> entry : DIRECTION_PROPS.entrySet()) {
            Direction dir = entry.getKey();
            boolean connect = dir != blocked
                    && SignPostNetworkUtil.shouldConnectTo(level,
                            level.getBlockState(pos.relative(dir)), pos.relative(dir), dir);
            state = state.setValue(entry.getValue(), connect);
        }
        return state;
    }

    // #region Interaction

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof RatchetWrenchItem)
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    // #region Redstone

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
            BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        if (level.isClientSide())
            return;

        BlockState liveState = level.getBlockState(pos);
        if (!liveState.is(this))
            return;

        // Update connection states; send to clients only to avoid cascading
        // neighborChanged calls.
        BlockState updated = setConnectionStates(liveState, level, pos);
        if (updated != liveState)
            level.setBlock(pos, updated, Block.UPDATE_CLIENTS);

        // Only react to non-network neighbors to avoid feedback from BFS
        // updateNeighborsAt calls.
        if (level.getBlockState(neighborPos).is(SignPostNetworkUtil.TAG_SIGN_POST_BLOCKS))
            return;

        BlockState current = level.getBlockState(pos);
        if (!current.is(this))
            return;

        boolean shouldBePowered = SignPostNetworkUtil.hasDirectPower(level, pos, current);
        boolean currentlyPowered = current.getValue(POWERED);
        if (currentlyPowered == shouldBePowered)
            return;

        if (!shouldBePowered
                && SignPostNetworkUtil.isNetworkDirectlyPowered(level, pos,
                        Services.CONFIG.signPostRelayMaxDepth()))
            return;

        SignPostNetworkUtil.propagatePower(level, pos, shouldBePowered,
                Services.CONFIG.signPostRelayMaxDepth());
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    // #region Shape

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SignPostNetworkUtil.buildShape(state, DIRECTION_PROPS);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return getShape(state, level, pos, ctx);
    }

    /** Fixtures are fully face-sturdy so blocks can attach to any face. */
    @Override
    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.block();
    }

    // #region Fluid

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    // #region Misc

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return !state.getValue(WATERLOGGED);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    protected static String getModeKey(String baseKey, String valueKey) {
        return Constants.MOD_ID + ".ratchet_wrench.mode." + baseKey + "." + valueKey;
    }

    protected static String getModeName(String baseKey, String valueKey) {
        String modeName = Component.translatable(getModeKey(baseKey, valueKey)).getString();
        String template = Component.translatable(getModeKey(baseKey, "name_template")).getString();
        return String.format(template, modeName);
    }

    // #region IWrenchConfigurable defaults

    @Override
    public int getWrenchModeCount(BlockState state) {
        return 0;
    }

    @Override
    public String getWrenchModeString(BlockState state, int modeIndex) {
        throw new UnsupportedOperationException("This fixture has no wrench modes");
    }

    @Override
    public String getWrenchModeKey(BlockState state, int modeIndex) {
        throw new UnsupportedOperationException("This fixture has no wrench modes");
    }

    @Override
    public Component getCurrentModeComponent(BlockState state, Player player) {
        return Component.empty();
    }

    @Override
    public InteractionResult onWrenchLeftClick(Level level, BlockPos pos, BlockState state, Player player) {
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult onWrenchRightClick(Level level, BlockPos pos, BlockState state, Player player) {
        return InteractionResult.PASS;
    }

    // #region EntityBlock

    @Override
    public abstract @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state);

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level,
            BlockState state, BlockEntityType<T> type) {
        return null;
    }
}
