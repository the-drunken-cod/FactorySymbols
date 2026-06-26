package com.drunkencod.symbols_n_signs.block.sign_post;

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
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SignPostLampFixtureBlock extends AbstractSignPostFixtureBlock {

    public static final String ID = "sign_post_lamp_fixture";

    public static final MapCodec<SignPostLampFixtureBlock> CODEC = RecordCodecBuilder
            .mapCodec(instance -> instance.group(propertiesCodec())
                    .apply((Applicative<Mu<SignPostLampFixtureBlock>, ?>) instance,
                            SignPostLampFixtureBlock::new));

    /** Signal mode: 0=ALWAYS_ON, 1=POWERED, 2=INVERTED, 3=ALWAYS_OFF */
    public static final IntegerProperty SIGNAL_MODE = IntegerProperty.create("signal_mode", 0, 3);

    /** True when the lamp should emit light. */
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    /** Orientation: Z = north/south (default), X = east/west */
    public static final net.minecraft.world.level.block.state.properties.EnumProperty<Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    // NS orientation: position 3,2,0 size 10,4,16
    private static final VoxelShape LAMP_SHAPE_Z = Block.box(3, 2, 0, 13, 6, 16);
    // EW orientation: rotated 90°
    private static final VoxelShape LAMP_SHAPE_X = Block.box(0, 2, 3, 16, 6, 13);

    public SignPostLampFixtureBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(SIGNAL_MODE, 0)
                .setValue(LIT, true)
                .setValue(AXIS, Axis.Z));
    }

    // #region Block states

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SIGNAL_MODE, LIT, AXIS);
    }

    @Override
    public Direction getFixtureDirection(BlockState state) {
        return Direction.DOWN;
    }

    // #region Placement

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        if (ctx.getClickedFace() != Direction.DOWN)
            return null;
        BlockState base = super.getStateForPlacement(ctx);
        if (base == null)
            return null;
        return base.setValue(FACE, AttachFace.CEILING);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(FACE) != AttachFace.CEILING)
            return false;
        return super.canSurvive(state, level, pos);
    }

    // #region Shape

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        VoxelShape lamp = state.getValue(AXIS) == Axis.X ? LAMP_SHAPE_X : LAMP_SHAPE_Z;
        return Shapes.or(SignPostNetworkUtil.buildShape(state, DIRECTION_PROPS), lamp);
    }

    // #region LIT logic

    public static boolean computeLit(BlockState state, boolean powered) {
        return switch (state.getValue(SIGNAL_MODE)) {
            case 0 -> true; // ALWAYS_ON
            case 1 -> powered; // POWERED
            case 2 -> !powered; // INVERTED
            case 3 -> false; // ALWAYS_OFF
            default -> false;
        };
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

    // #region Wrench configuration

    private static final int MODE_ORIENTATION = 0;
    private static final int MODE_SIGNAL = 1;
    private static final int MODE_COUNT = 2;

    @Override
    public int getWrenchModeCount(BlockState state, Direction clickedFace) {
        return MODE_COUNT;
    }

    @Override
    public String getWrenchModeKey(BlockState state, Direction clickedFace, int modeIndex) {
        return switch (modeIndex) {
            case MODE_ORIENTATION -> getModeKey("lamp_fixture", "orientation");
            case MODE_SIGNAL -> getModeKey("lamp_fixture", "signal_mode");
            default -> Constants.MOD_ID + ".ratchet_wrench.mode.unknown";
        };
    }

    @Override
    public String getWrenchModeString(BlockState state, Direction clickedFace, int modeIndex) {
        return switch (modeIndex) {
            case MODE_ORIENTATION -> getModeName("lamp_fixture", "orientation");
            case MODE_SIGNAL -> getModeName("lamp_fixture", "signal_mode");
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
                    Axis next = state.getValue(AXIS) == Axis.Z ? Axis.X : Axis.Z;
                    level.setBlock(pos, state.setValue(AXIS, next), Block.UPDATE_CLIENTS);
                    // Fixture direction (DOWN) is unaffected by AXIS, so connection states stay
                    // valid
                    player.displayClientMessage(
                            Component.translatable(getWrenchModeKey(state, clickedFace, MODE_ORIENTATION))
                                    .append(": ")
                                    .append(Component.translatable(
                                            getWrenchModeKey(state, clickedFace, MODE_ORIENTATION) + ".value."
                                                    + next.getName())),
                            true);
                }
                case MODE_SIGNAL -> {
                    int next = (state.getValue(SIGNAL_MODE) + 1) % 4;
                    BlockState newState = state.setValue(SIGNAL_MODE, next);
                    boolean newLit = computeLit(newState, state.getValue(POWERED));
                    level.setBlock(pos, newState.setValue(LIT, newLit), Block.UPDATE_CLIENTS);
                    player.displayClientMessage(
                            Component.translatable(getWrenchModeKey(state, clickedFace, MODE_SIGNAL))
                                    .append(": ")
                                    .append(Component.translatable(
                                            getWrenchModeKey(state, clickedFace, MODE_SIGNAL) + ".value." + next)),
                            true);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    // #region BlockEntity

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SignPostLampFixtureBlockEntity(pos, state);
    }

    @Override
    protected MapCodec<SignPostLampFixtureBlock> codec() {
        return CODEC;
    }
}
