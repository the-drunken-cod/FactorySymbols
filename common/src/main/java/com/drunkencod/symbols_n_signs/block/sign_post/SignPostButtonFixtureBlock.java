package com.drunkencod.symbols_n_signs.block.sign_post;

import java.util.List;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.item.RatchetWrenchItem;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SignPostButtonFixtureBlock extends AbstractSignPostFixtureBlock {

    public static final String ID = "sign_post_button_fixture";

    public static final MapCodec<SignPostButtonFixtureBlock> CODEC = RecordCodecBuilder
            .mapCodec(instance -> instance.group(propertiesCodec())
                    .apply((Applicative<Mu<SignPostButtonFixtureBlock>, ?>) instance,
                            SignPostButtonFixtureBlock::new));

    /**
     * When true (default), the block emits a redstone signal when physically
     * pressed.
     * When false, it emits when NOT pressed (active-low / normally-on output).
     */
    public static final BooleanProperty ACTIVE_HIGH = BooleanProperty.create("active_high");

    /**
     * True while the button is physically depressed (distinct from relay POWERED
     * state).
     */
    public static final BooleanProperty PRESSED = BooleanProperty.create("pressed");

    // #region Shape

    /**
     * 6x10x6 px button body shapes per horizontal facing, biased 1 px toward FACING
     */
    private static final VoxelShape BUTTON_NORTH = Block.box(5, 3, 4, 11, 13, 10);
    private static final VoxelShape BUTTON_SOUTH = Block.box(5, 3, 6, 11, 13, 12);
    private static final VoxelShape BUTTON_EAST = Block.box(6, 3, 5, 12, 13, 11);
    private static final VoxelShape BUTTON_WEST = Block.box(4, 3, 5, 10, 13, 11);

    private static VoxelShape getButtonBody(Direction facing) {
        return switch (facing) {
            case SOUTH -> BUTTON_SOUTH;
            case EAST -> BUTTON_EAST;
            case WEST -> BUTTON_WEST;
            default -> BUTTON_NORTH;
        };
    }

    public SignPostButtonFixtureBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(ACTIVE_HIGH, true).setValue(PRESSED, false));
    }

    // #region Block states

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVE_HIGH, PRESSED);
    }

    @Override
    public Direction getFixtureDirection(BlockState state) {
        return state.getValue(FACING);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return Shapes.or(SignPostNetworkUtil.buildShape(state, DIRECTION_PROPS), getButtonBody(state.getValue(FACING)));
    }

    // #region Placement

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState base = super.getStateForPlacement(ctx);
        if (base == null)
            return null;
        Direction clicked = ctx.getClickedFace();
        if (clicked.getAxis() == Direction.Axis.Y)
            return null;
        return base.setValue(FACE, AttachFace.WALL).setValue(FACING, clicked);
    }

    @Override
    public boolean canSurvive(BlockState state, net.minecraft.world.level.LevelReader level, BlockPos pos) {
        // Only allow wall placement (no top/bottom buttons on sign posts)
        // Only allow wall placement (no top/bottom buttons on sign posts)
        if (state.getValue(FACE) != AttachFace.WALL)
            return false;
        return super.canSurvive(state, level, pos);
    }

    // #region Interaction

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hit) {
        if (state.getValue(PRESSED))
            return InteractionResult.CONSUME;
        level.setBlock(pos, state.setValue(PRESSED, true), Block.UPDATE_CLIENTS);
        level.scheduleTick(pos, this, 20);
        level.playSound(player, pos, SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.BLOCKS, 0.8f, 1f);
        level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
        evaluateAndPropagate(level, pos);
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState live = level.getBlockState(pos);
        if (!live.is(this) || !live.getValue(PRESSED))
            return;
        level.setBlock(pos, live.setValue(PRESSED, false), Block.UPDATE_CLIENTS);
        level.playSound(null, pos, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundSource.BLOCKS, 0.8f, 1f);
        evaluateAndPropagate(level, pos);
    }

    // #region Redstone

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide() && !oldState.is(this))
            evaluateAndPropagate(level, pos);
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        boolean pressed = state.getValue(PRESSED);
        boolean activeHigh = state.getValue(ACTIVE_HIGH);
        return (activeHigh ? pressed : !pressed) ? 15 : 0;
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        // Emit strong signal from the face the fixture is attached to (the post's face
        // behind it)
        Direction fixtureDir = getFixtureDirection(state);
        return direction == fixtureDir.getOpposite() ? getSignal(state, level, pos, direction) : 0;
    }

    // #region IWrenchConfigurable

    /** Wrench mode indices */
    private static final int MODE_ORIENTATION = 0;
    private static final int MODE_ACTIVE_HIGH = 1;
    private static final int MODE_COUNT = 2;

    private static final List<Direction> HORIZONTAL_DIRS = List.of(
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);

    @Override
    public int getWrenchModeCount(BlockState state) {
        return MODE_COUNT;
    }

    @Override
    public String getWrenchModeKey(BlockState state, int modeIndex) {
        return switch (modeIndex) {
            case MODE_ORIENTATION -> getModeKey("button_fixture", "orientation");
            case MODE_ACTIVE_HIGH -> getModeKey("button_fixture", "active_high");
            default -> Constants.MOD_ID + ".ratchet_wrench.mode.unknown";
        };
    }

    @Override
    public String getWrenchModeString(BlockState state, int modeIndex) {
        return switch (modeIndex) {
            case MODE_ORIENTATION -> getModeName("button_fixture", "orientation");
            case MODE_ACTIVE_HIGH -> getModeName("button_fixture", "active_high");
            default -> Constants.MOD_ID + ".ratchet_wrench.mode.unknown";
        };
    }

    @Override
    public Component getCurrentModeComponent(BlockState state, Player player) {
        ItemStack wrench = RatchetWrenchItem.getWrenchInHand(player);
        if (wrench.isEmpty())
            return Component.empty();
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(this);
        int mode = RatchetWrenchItem.getSelectedMode(wrench, blockId);
        return Component.literal(getWrenchModeString(state, mode));
    }

    @Override
    public InteractionResult onWrenchLeftClick(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            ItemStack wrench = RatchetWrenchItem.getWrenchInHand(player);
            if (wrench.isEmpty())
                return InteractionResult.PASS;
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(this);
            int next = (RatchetWrenchItem.getSelectedMode(wrench, blockId) + 1) % MODE_COUNT;
            RatchetWrenchItem.setSelectedMode(wrench, blockId, next);
            player.displayClientMessage(Component.literal(getWrenchModeString(state, next)), true);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult onWrenchRightClick(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            ItemStack wrench = RatchetWrenchItem.getWrenchInHand(player);
            if (wrench.isEmpty())
                return InteractionResult.PASS;
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(this);
            int mode = RatchetWrenchItem.getSelectedMode(wrench, blockId);
            switch (mode) {
                case MODE_ORIENTATION -> {
                    int idx = HORIZONTAL_DIRS.indexOf(state.getValue(FACING));
                    Direction next = HORIZONTAL_DIRS.get((idx + 1) % HORIZONTAL_DIRS.size());
                    BlockState newState = setConnectionStates(state.setValue(FACING, next), level, pos);
                    level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
                    player.displayClientMessage(
                            Component.translatable(getWrenchModeKey(state, MODE_ORIENTATION))
                                    .append(": ")
                                    .append(Component.translatable(
                                            getWrenchModeKey(state, MODE_ORIENTATION) + ".value." + next.getName())),
                            true);
                }
                case MODE_ACTIVE_HIGH -> {
                    boolean next = !state.getValue(ACTIVE_HIGH);
                    level.setBlock(pos, state.setValue(ACTIVE_HIGH, next), Block.UPDATE_CLIENTS);
                    evaluateAndPropagate(level, pos);
                    player.displayClientMessage(
                            Component.translatable(getWrenchModeKey(state, MODE_ACTIVE_HIGH))
                                    .append(": ")
                                    .append(Component.translatable(
                                            getWrenchModeKey(state, MODE_ACTIVE_HIGH) + ".value." + (next ? 1 : 0))),
                            true);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    // #region BlockEntity

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SignPostButtonFixtureBlockEntity(pos, state);
    }

    @Override
    protected MapCodec<SignPostButtonFixtureBlock> codec() {
        return CODEC;
    }
}
