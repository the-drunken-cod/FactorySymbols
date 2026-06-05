package com.drunkencod.factory_symbols.block.sign_post;

import com.drunkencod.factory_symbols.platform.Services;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SignPostBlock extends PipeBlock implements SimpleWaterloggedBlock {

    public static final String ID = "sign_post";

    /** Thickness of the sign post's segments in pixels */
    public static final float APOTHEM = SignPostNetworkUtil.APOTHEM;

    // Forwarded for backward-compat; canonical definitions live in
    // SignPostNetworkUtil.
    public static final TagKey<Block> TAG_DOES_NOT_CONNECT_TO = SignPostNetworkUtil.TAG_DOES_NOT_CONNECT_TO;
    public static final TagKey<Block> TAG_CONNECTS_TO = SignPostNetworkUtil.TAG_CONNECTS_TO;
    public static final TagKey<Block> TAG_CONNECTS_TO_BOTTOM_FACE = SignPostNetworkUtil.TAG_CONNECTS_TO_BOTTOM_FACE;
    public static final TagKey<Block> TAG_CONNECTS_TO_TOP_FACE = SignPostNetworkUtil.TAG_CONNECTS_TO_TOP_FACE;
    public static final TagKey<Block> TAG_CONNECTS_TO_SIDES = SignPostNetworkUtil.TAG_CONNECTS_TO_SIDES;

    public static final MapCodec<SignPostBlock> CODEC = SignPostBlock.simpleCodec(SignPostBlock::new);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public SignPostBlock(Properties properties) {
        super(APOTHEM, properties.forceSolidOn());
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, false).setValue(EAST, false)
                .setValue(SOUTH, false).setValue(WEST, false).setValue(UP, false).setValue(DOWN, false)
                .setValue(WATERLOGGED, false).setValue(POWERED, false));
    }

    /**
     * sign post is fully face-sturdy so buttons, levers, etc. can attach to any
     * face
     */
    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.block();
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext ctx) {
        return ctx.getItemInHand().getItem() instanceof BlockItem bi
                && bi.getBlock() instanceof AbstractSignPostFixtureBlock;
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, WATERLOGGED, POWERED);
    }

    @Override
    protected BlockState updateShape(BlockState ownState, Direction direction, BlockState neighborState,
            LevelAccessor level, BlockPos ownPos, BlockPos neighborPos) {
        if (!ownState.canSurvive(level, ownPos))
            level.scheduleTick(ownPos, this, 1);

        if (ownState.getValue(WATERLOGGED))
            level.scheduleTick(ownPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

        boolean shouldConnect = SignPostNetworkUtil.shouldConnectTo(level, neighborState, neighborPos, direction);
        return ownState.setValue(PROPERTY_BY_DIRECTION.get(direction), shouldConnect);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        LevelAccessor level = ctx.getLevel();
        boolean waterlogged = level.getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER;
        return getStateWithConnections(level, ctx.getClickedPos(),
                this.defaultBlockState())
                .setValue(WATERLOGGED, waterlogged);
    }

    public static BlockState getStateWithConnections(LevelAccessor level, BlockPos blockPos,
            BlockState blockState) {
        return blockState
                .trySetValue(DOWN, SignPostNetworkUtil.shouldConnectTo(level,
                        level.getBlockState(blockPos.below()), blockPos.below(), Direction.DOWN))
                .trySetValue(UP, SignPostNetworkUtil.shouldConnectTo(level,
                        level.getBlockState(blockPos.above()), blockPos.above(), Direction.UP))
                .trySetValue(NORTH, SignPostNetworkUtil.shouldConnectTo(level,
                        level.getBlockState(blockPos.north()), blockPos.north(), Direction.NORTH))
                .trySetValue(EAST, SignPostNetworkUtil.shouldConnectTo(level,
                        level.getBlockState(blockPos.east()), blockPos.east(), Direction.EAST))
                .trySetValue(SOUTH, SignPostNetworkUtil.shouldConnectTo(level,
                        level.getBlockState(blockPos.south()), blockPos.south(), Direction.SOUTH))
                .trySetValue(WEST, SignPostNetworkUtil.shouldConnectTo(level,
                        level.getBlockState(blockPos.west()), blockPos.west(), Direction.WEST));
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
            BlockPos neighborPos, boolean movedByPiston) {
        if (!state.canSurvive(level, pos))
            level.scheduleTick(pos, this, 1);

        if (state.getValue(WATERLOGGED))
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

        // Re-validate all connections whenever any neighbor changes and push the
        // update to clients. UPDATE_CLIENTS avoids a second neighborChanged wave.
        BlockState updated = getStateWithConnections(level, pos, state);
        if (updated != state)
            level.setBlock(pos, updated, Block.UPDATE_CLIENTS);

        // Relay: propagate power state change to connected sign post network.
        // Only react to non-network neighbors to avoid feedback from BFS
        // updateNeighborsAt calls.
        if (!level.isClientSide() && !level.getBlockState(neighborPos).is(SignPostNetworkUtil.TAG_SIGN_POST_BLOCKS)) {
            BlockState liveState = level.getBlockState(pos);
            boolean shouldBePowered = SignPostNetworkUtil.hasDirectPower(level, pos, liveState);
            // When powering down, verify no other node in the network still has a direct
            // signal — secondary updates from comparators/dust reading our powered state
            // would otherwise incorrectly collapse the network.
            if (liveState.is(this) && liveState.getValue(POWERED) != shouldBePowered)
                if (shouldBePowered || !SignPostNetworkUtil.isNetworkDirectlyPowered(level, pos,
                        Services.CONFIG.signPostRelayMaxDepth()))
                    SignPostNetworkUtil.propagatePower(level, pos, shouldBePowered,
                            Services.CONFIG.signPostRelayMaxDepth());
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    // fix other posts occluding light propagation, causing "color banding":
    @Override
    protected boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return !blockState.getValue(WATERLOGGED);
    }

    @Override
    protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected MapCodec<? extends PipeBlock> codec() {
        return CODEC;
    }
}
