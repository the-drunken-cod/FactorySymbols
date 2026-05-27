package com.drunkencod.factory_symbols.block.sign_post;

import com.drunkencod.factory_symbols.Constants;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class SignPostButtonFixtureBlock extends AbstractSignPostFixtureBlock {

    public static final MapCodec<SignPostButtonFixtureBlock> CODEC = RecordCodecBuilder
            .mapCodec(instance -> instance.group(propertiesCodec())
                    .apply((Applicative<Mu<SignPostButtonFixtureBlock>, ?>) instance,
                            SignPostButtonFixtureBlock::new));

    /**
     * When true (default), the block emits a redstone signal when the sign post
     * network is powered. When false, it emits when the network is NOT powered
     * (active-low / inverted output).
     */
    public static final BooleanProperty ACTIVE_HIGH = BooleanProperty.create("active_high");

    public SignPostButtonFixtureBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(ACTIVE_HIGH, true));
    }

    // #region Block states

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVE_HIGH);
    }

    @Override
    public Direction getFixtureDirection(BlockState state) {
        // Button fixture is WALL-only: the fixture element faces outward in FACING
        // direction
        return state.getValue(FACING);
    }

    // #region Placement

    @Override
    public boolean canSurvive(BlockState state, net.minecraft.world.level.LevelReader level, BlockPos pos) {
        // Only allow wall placement (no top/bottom buttons on sign posts)
        if (state.getValue(FACE) != AttachFace.WALL)
            return false;
        return super.canSurvive(state, level, pos);
    }

    // #region Redstone

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        boolean powered = state.getValue(POWERED);
        boolean activeHigh = state.getValue(ACTIVE_HIGH);
        return (activeHigh ? powered : !powered) ? 15 : 0;
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

    @Override
    public int getWrenchModeCount(BlockState state) {
        return MODE_COUNT;
    }

    @Override
    public String getWrenchModeKey(BlockState state, int modeIndex) {
        return switch (modeIndex) {
            case MODE_ORIENTATION -> Constants.MOD_ID + ".ratchet_wrench.mode.button_fixture.orientation";
            case MODE_ACTIVE_HIGH -> Constants.MOD_ID + ".ratchet_wrench.mode.button_fixture.active_high";
            default -> Constants.MOD_ID + ".ratchet_wrench.mode.unknown";
        };
    }

    @Override
    public Component getCurrentModeComponent(BlockState state, Player player) {
        // Selected mode index is stored in the wrench item's NBT (handled by the wrench
        // item when implemented)
        return Component.translatable(Constants.MOD_ID + ".ratchet_wrench.mode.button_fixture.orientation");
    }

    @Override
    public InteractionResult onWrenchLeftClick(Level level, BlockPos pos, BlockState state, Player player) {
        // Cycle selected mode — mode selection is managed by the wrench item itself
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult onWrenchRightClick(Level level, BlockPos pos, BlockState state, Player player) {
        // Cycle the currently-selected mode's value on the block state
        // (The wrench item will call this and pass which mode is active via its own
        // NBT.)
        // For now, default to cycling ACTIVE_HIGH as a placeholder:
        if (!level.isClientSide()) {
            boolean current = state.getValue(ACTIVE_HIGH);
            level.setBlock(pos, state.setValue(ACTIVE_HIGH, !current), Block.UPDATE_ALL);
            player.displayClientMessage(
                    Component.translatable(Constants.MOD_ID + ".ratchet_wrench.mode.button_fixture.active_high")
                            .append(": ")
                            .append(Component.translatable(Constants.MOD_ID
                                    + ".ratchet_wrench.mode.button_fixture.active_high.value."
                                    + (!current ? 1 : 0))),
                    true);
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
