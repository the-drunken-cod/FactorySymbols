package com.drunkencod.symbols_n_signs.block.sign_post;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.item.RatchetWrenchItem;
import com.drunkencod.symbols_n_signs.platform.Services;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SignPostRedstoneEmitterFixtureBlock extends AbstractSignPostFixtureBlock {

    public static final String ID = "sign_post_redstone_emitter_fixture";

    public static final MapCodec<SignPostRedstoneEmitterFixtureBlock> CODEC = RecordCodecBuilder
            .mapCodec(instance -> instance.group(propertiesCodec())
                    .apply((Applicative<Mu<SignPostRedstoneEmitterFixtureBlock>, ?>) instance,
                            SignPostRedstoneEmitterFixtureBlock::new));
    /**
     * Whether the output signal should be inverted
     */
    public static final BooleanProperty INVERTED = BooleanProperty.create("inverted");

    /**
     * Whether the texture should be lit asf
     */
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    private static final VoxelShape SHAPE_NORTH = Block.box(5, 5, 0, 11, 11, 6);
    private static final VoxelShape SHAPE_EAST = Block.box(10, 5, 5, 16, 11, 11);
    private static final VoxelShape SHAPE_SOUTH = Block.box(5, 5, 10, 11, 11, 16);
    private static final VoxelShape SHAPE_WEST = Block.box(0, 5, 5, 6, 11, 11);
    private static final VoxelShape SHAPE_UP = Block.box(5, 10, 5, 11, 16, 11);
    private static final VoxelShape SHAPE_DOWN = Block.box(5, 0, 5, 11, 6, 11);

    public SignPostRedstoneEmitterFixtureBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(INVERTED, false).setValue(LIT, false));
    }

    // #region Block states

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(INVERTED, LIT);
    }

    @Override
    public Direction getFixtureDirection(BlockState state) {
        AttachFace face = state.getValue(FACE);
        if (face == AttachFace.WALL)
            return state.getValue(FACING);
        return face == AttachFace.CEILING ? Direction.UP : Direction.DOWN;
    }

    public static boolean computeLit(BlockState state, boolean powered) {
        return powered ^ state.getValue(INVERTED);
    }

    public static boolean computeLit(BlockState state) {
        return computeLit(state, state.getValue(POWERED));
    }

    // #region Placement

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState base = super.getStateForPlacement(ctx);

        if (base == null)
            return null;

        Direction clicked = ctx.getClickedFace();

        if (clicked.getAxis() == Direction.Axis.Y)
            return base
                    .setValue(FACE, clicked.getAxisDirection() == AxisDirection.POSITIVE
                            ? AttachFace.CEILING
                            : AttachFace.FLOOR)
                    .setValue(FACING, Direction.SOUTH);
        else
            return base
                    .setValue(FACE, AttachFace.WALL)
                    .setValue(FACING, clicked);
    }

    // #region Shape

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        VoxelShape emitterShape = switch (state.getValue(FACE)) {
            case FLOOR -> SHAPE_DOWN;
            case CEILING -> SHAPE_UP;
            default -> (switch (state.getValue(FACING)) {
                case NORTH -> SHAPE_NORTH;
                case SOUTH -> SHAPE_SOUTH;
                case EAST -> SHAPE_EAST;
                case WEST -> SHAPE_WEST;
                default -> SHAPE_NORTH;
            });
        };
        return Shapes.or(SignPostNetworkUtil.buildShape(state, DIRECTION_PROPS), emitterShape);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide() && !oldState.is(this)) {
            boolean networkPowered = SignPostNetworkUtil.isNetworkDirectlyPowered(
                    level, pos, Services.CONFIG.signPostRelayMaxDepth());
            SignPostNetworkUtil.propagatePower(level, pos, networkPowered,
                    Services.CONFIG.signPostRelayMaxDepth());
        }
    }

    @Override
    protected BlockState updateShape(BlockState ownState, Direction direction, BlockState neighborState,
            LevelAccessor level, BlockPos ownPos, BlockPos neighborPos) {
        BlockState state = super.updateShape(ownState, direction, neighborState, level, ownPos, neighborPos);

        return state.setValue(LIT, computeLit(state));
    }

    // #region Redstone

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if (!state.getValue(LIT))
            return 0;
        // direction is queried FROM the neighbor TOWARD this block, so invert
        return direction == getFixtureDirection(state).getOpposite() ? 15 : 0;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return getDirectSignal(state, level, pos, direction);
    }

    // #region Wrench configuration

    private static final int MODE_ORIENTATION = 0;
    private static final int MODE_INVERTED = 1;
    private static final int MODE_COUNT = 2;

    @Override
    public int getWrenchModeCount(BlockState state, Direction clickedFace) {
        return MODE_COUNT;
    }

    @Override
    public String getWrenchModeKey(BlockState state, Direction clickedFace, int modeIndex) {
        return switch (modeIndex) {
            case MODE_ORIENTATION -> getModeKey("redstone_emitter_fixture", "orientation");
            case MODE_INVERTED -> getModeKey("redstone_emitter_fixture", "inverted");
            default -> Constants.MOD_ID + ".ratchet_wrench.mode.unknown";
        };
    }

    @Override
    public String getWrenchModeString(BlockState state, Direction clickedFace, int modeIndex) {
        return switch (modeIndex) {
            case MODE_ORIENTATION -> getModeName("redstone_emitter_fixture", "orientation");
            case MODE_INVERTED -> getModeName("redstone_emitter_fixture", "inverted");
            default -> Constants.MOD_ID + ".ratchet_wrench.mode.unknown";
        };
    }

    @Override
    public Component getCurrentModeComponent(BlockState state, Direction clickedFace, Player player) {
        ItemStack wrench = RatchetWrenchItem.getWrenchInHand(player);
        if (wrench.isEmpty())
            return Component.empty();
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(this);
        int mode = RatchetWrenchItem.getSelectedMode(wrench, blockId);
        return Component.literal(getWrenchModeString(state, clickedFace, mode));
    }

    @Override
    public InteractionResult onWrenchLeftClick(Level level, BlockPos pos, BlockState state, Direction clickedFace,
            Vec3 hitLocation, Player player) {
        if (!level.isClientSide()) {
            ItemStack wrench = RatchetWrenchItem.getWrenchInHand(player);
            if (wrench.isEmpty())
                return InteractionResult.PASS;
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(this);
            int next = (RatchetWrenchItem.getSelectedMode(wrench, blockId) + 1) % MODE_COUNT;
            RatchetWrenchItem.setSelectedMode(wrench, blockId, next);
            player.displayClientMessage(Component.literal(getWrenchModeString(state, clickedFace, next)), true);
        }
        return InteractionResult.SUCCESS;
    }

    protected static final List<Tuple<AttachFace, Direction>> FACE_DIRECTION_LIST = List.of(
            new Tuple<AttachFace, Direction>(AttachFace.WALL, Direction.NORTH),
            new Tuple<AttachFace, Direction>(AttachFace.WALL, Direction.SOUTH),
            new Tuple<AttachFace, Direction>(AttachFace.WALL, Direction.EAST),
            new Tuple<AttachFace, Direction>(AttachFace.WALL, Direction.WEST),
            new Tuple<AttachFace, Direction>(AttachFace.FLOOR, Direction.SOUTH),
            new Tuple<AttachFace, Direction>(AttachFace.CEILING, Direction.SOUTH));

    @Override
    public InteractionResult onWrenchRightClick(Level level, BlockPos pos, BlockState state, Direction clickedFace,
            Vec3 hitLocation, Player player) {
        if (!level.isClientSide()) {
            ItemStack wrench = RatchetWrenchItem.getWrenchInHand(player);
            if (wrench.isEmpty())
                return InteractionResult.PASS;
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(this);
            int mode = RatchetWrenchItem.getSelectedMode(wrench, blockId);
            switch (mode) {
                case MODE_ORIENTATION -> {
                    AttachFace currentFace = state.getValue(FACE);
                    Direction currentDir = state.getValue(FACING);

                    @Nullable
                    AttachFace nextFace = null;
                    @Nullable
                    Direction nextDir = null;

                    int i = 0;

                    for (Tuple<AttachFace, Direction> tuple : FACE_DIRECTION_LIST) {
                        AttachFace face = tuple.getA();
                        Direction dir = tuple.getB();

                        i++;

                        if (currentFace == face && currentDir == dir) {
                            i = i % FACE_DIRECTION_LIST.size();

                            nextFace = FACE_DIRECTION_LIST.get(i).getA();
                            nextDir = FACE_DIRECTION_LIST.get(i).getB();
                        }
                    }

                    if (nextFace == null || nextDir == null) {
                        Constants.LOG.error(
                                "Couldn't parse orientation in Ratchet Wrench right click action for block at position {}",
                                pos);
                        return InteractionResult.FAIL;
                    }

                    BlockState newState = setConnectionStates(
                            state.setValue(FACE, nextFace).setValue(FACING, nextDir), level, pos);
                    level.setBlock(pos, newState, Block.UPDATE_CLIENTS);

                    player.displayClientMessage(
                            Component.translatable(getWrenchModeKey(state, clickedFace, MODE_ORIENTATION))
                                    .append(": ")
                                    .append(Component.translatable(
                                            nextFace == AttachFace.WALL
                                                    ? getWrenchModeKey(state, clickedFace, MODE_ORIENTATION)
                                                            + ".value." + nextDir.toString().toLowerCase()
                                                    : getWrenchModeKey(state, clickedFace, MODE_ORIENTATION)
                                                            + ".value." + nextFace.toString().toLowerCase())),
                            true);
                }
                case MODE_INVERTED -> {
                    boolean isInverted = state.getValue(INVERTED);
                    BlockState invState = state.setValue(INVERTED, !isInverted);

                    level.setBlock(pos, invState.setValue(LIT, computeLit(invState)), Block.UPDATE_ALL);

                    player.displayClientMessage(
                            Component.translatable(getWrenchModeKey(state, clickedFace, MODE_INVERTED))
                                    .append(": ")
                                    .append(Component.translatable(
                                            getWrenchModeKey(state, clickedFace, MODE_INVERTED) + ".value." +
                                                    (isInverted ? "0" : "1"))),
                            true);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    // #region BlockEntity

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SignPostRedstoneEmitterFixtureBlockEntity(pos, state);
    }

    @Override
    protected MapCodec<SignPostRedstoneEmitterFixtureBlock> codec() {
        return CODEC;
    }
}
