package com.drunkencod.symbols_n_signs.block.sign_post;

import java.util.List;
import java.util.Set;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.item.RatchetWrenchItem;
import com.drunkencod.symbols_n_signs.item.SignItem;
import com.drunkencod.symbols_n_signs.registry.ModSoundEvents;
import com.drunkencod.symbols_n_signs.signs.SignStance;
import com.drunkencod.symbols_n_signs.signs.SignSupportType;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Fixture that can hold an independent sign item on each of the
 * post's 6 faces simultaneously. Unlike the single-face fixtures (Button,
 * Lamp, Redstone Emitter), this block occupies an arbitrary subset of faces
 * at once - see ADR 0001. {@code FACE}/{@code FACING} (inherited from
 * {@code FaceAttachedHorizontalDirectionalBlock} via the abstract base class)
 * are vestigial for this block and are never read.
 */
public class SignPostSignFixtureBlock extends AbstractSignPostFixtureBlock {

    public static final String ID = "sign_post_sign_fixture";

    public static final MapCodec<SignPostSignFixtureBlock> CODEC = RecordCodecBuilder
            .mapCodec(instance -> instance.group(propertiesCodec())
                    .apply((Applicative<Mu<SignPostSignFixtureBlock>, ?>) instance,
                            SignPostSignFixtureBlock::new));

    public SignPostSignFixtureBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction getFixtureDirection(BlockState state) {
        // Multi-face: nothing in the shared connection/shape logic calls this for
        // SignPostSignFixtureBlock anymore (it consults isFaceOccupied instead).
        throw new UnsupportedOperationException(
                "Sign Fixture occupies multiple faces; use isFaceOccupied(level, pos, state, direction) instead");
    }

    /**
     * Occupancy is read straight from the BlockEntity rather than mirrored into
     * blockstate properties — see the note on
     * {@link AbstractSignPostFixtureBlock#isFaceOccupied} for why (a per-face
     * bitset in blockstate would multiply this block's state count by 64).
     */
    @Override
    public boolean isFaceOccupied(BlockGetter level, BlockPos pos, BlockState state, Direction direction) {
        return level.getBlockEntity(pos) instanceof SignPostSignFixtureBlockEntity be && be.isOccupied(direction);
    }

    // #region Shape

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        VoxelShape shape = SignPostNetworkUtil.buildShape(state, DIRECTION_PROPS);
        if (level.getBlockEntity(pos) instanceof SignPostSignFixtureBlockEntity be)
            for (Direction dir : be.getOccupiedFaces()) {
                SignFixtureFaceData data = be.getFaceData(dir);
                if (data != null)
                    shape = Shapes.or(shape, buildPaneBump(dir, data));
            }
        return shape;
    }

    /**
     * Bounding box of a face's two panes (front + back), derived from the same
     * Stance/Rotation/Scale transform the renderer uses, so the collision shape
     * always matches what's drawn. See {@link SignFixtureGeometry}.
     * <p>
     * Clamped to the block's own unit cube: collision and raycasting are both
     * keyed by block position (only the shape of the block actually being
     * visited is ever consulted), so any part of a shape that spills into a
     * neighboring cell is invisible to both from that neighbor's side —
     * causing exactly the rubberbanding/forced-crawl/partial-clipping
     * symptoms a large Scale or perpendicular Stance can produce. Clamping
     * means the overflowing visual portion simply isn't solid or targetable,
     * which is the safer tradeoff.
     */
    private static VoxelShape buildPaneBump(Direction face, SignFixtureFaceData data) {
        Matrix4f pivot = SignFixtureGeometry.buildPivotTransform(face, data);
        Matrix4f frontPane = SignFixtureGeometry.buildPaneTransform(pivot, data.getScale(), 1);
        Matrix4f backPane = SignFixtureGeometry.buildPaneTransform(pivot, data.getScale(), -1);

        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE, maxZ = -Float.MAX_VALUE;
        for (Matrix4f pane : new Matrix4f[] { frontPane, backPane }) {
            for (Vector3f corner : SignFixtureGeometry.unitQuadCorners()) {
                Vector3f p = SignFixtureGeometry.transform(pane, corner);
                minX = Math.min(minX, p.x());
                minY = Math.min(minY, p.y());
                minZ = Math.min(minZ, p.z());
                maxX = Math.max(maxX, p.x());
                maxY = Math.max(maxY, p.y());
                maxZ = Math.max(maxZ, p.z());
            }
        }
        minX = Mth.clamp(minX, 0f, 1f);
        minY = Mth.clamp(minY, 0f, 1f);
        minZ = Mth.clamp(minZ, 0f, 1f);
        maxX = Mth.clamp(maxX, 0f, 1f);
        maxY = Mth.clamp(maxY, 0f, 1f);
        maxZ = Mth.clamp(maxZ, 0f, 1f);
        return Shapes.create(new AABB(minX, minY, minZ, maxX, maxY, maxZ));
    }

    /**
     * Which occupied face the wrench actually hit, determined from the exact
     * impact point rather than the block's outer-cube face: a sign's pane often
     * doesn't lie flush with that outer face (a perpendicular Stance, or the
     * backside of a double-sided sign), so the literal {@code clickedFace} the
     * game reports frequently doesn't match the fixture face being aimed at.
     * Falls back to the occupied face whose pane is nearest the hit point if
     * none of them strictly contain it (e.g. a hit landing exactly on a
     * boundary plane) - unless {@code allowNearestFallback} is {@code false},
     * in which case a hit that isn't strictly inside any pane (i.e. it landed
     * on the post itself) resolves to {@code null} instead.
     */
    private static @Nullable Direction resolveTargetFace(BlockPos pos, SignPostSignFixtureBlockEntity be,
            @Nullable Vec3 hitLocation, boolean allowNearestFallback) {
        if (hitLocation == null)
            return null;
        Vec3 local = hitLocation.subtract(pos.getX(), pos.getY(), pos.getZ());

        Direction nearest = null;
        double nearestDistSq = Double.MAX_VALUE;
        for (Direction face : be.getOccupiedFaces()) {
            SignFixtureFaceData data = be.getFaceData(face);
            if (data == null)
                continue;
            AABB box = buildPaneBump(face, data).bounds();
            if (box.contains(local.x, local.y, local.z))
                return face;
            double distSq = box.distanceToSqr(local);
            if (distSq < nearestDistSq) {
                nearestDistSq = distSq;
                nearest = face;
            }
        }
        return allowNearestFallback ? nearest : null;
    }

    // #region Interaction - placing a sign onto an unoccupied face

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {
        ItemInteractionResult wrenchResult = super.useItemOn(stack, state, level, pos, player, hand, hit);
        if (wrenchResult != ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION)
            return wrenchResult;

        if (!(stack.getItem() instanceof SignItem))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        Direction face = hit.getDirection();
        if (!(level.getBlockEntity(pos) instanceof SignPostSignFixtureBlockEntity be) || be.isOccupied(face))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        ItemStack toStore = stack.copyWithCount(1);
        be.setFaceData(face, SignFixtureFaceData.initial(toStore));

        BlockState newState = setConnectionStates(state, level, pos);
        level.setBlock(pos, newState, Block.UPDATE_CLIENTS);

        if (!player.isCreative())
            stack.shrink(1);

        playAddItemSound(level, pos, player);
        level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);

        Constants.LOG.info("Placed sign {} on face {} of Sign Fixture at {}", toStore.getItem(), face, pos);
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    // #region Interaction - removing a single face's sign with an empty hand

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hit) {
        if (!player.isShiftKeyDown())
            return InteractionResult.PASS;

        if (!(level.getBlockEntity(pos) instanceof SignPostSignFixtureBlockEntity be))
            return InteractionResult.PASS;
        Direction face = resolveTargetFace(pos, be, hit.getLocation(), true);
        if (face == null || !be.isOccupied(face))
            return InteractionResult.PASS;

        removeSingleSign(level, pos, state, be, face, player);
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    /**
     * Removes one face's sign, gives it back to the player and updates the
     * block's connections/shape. Targeting a specific sign only ever removes
     * that sign - even if it's the last one left - and never reverts the
     * fixture itself; reverting to a plain Sign Post only happens when the
     * post (not a sign) is targeted, via {@link #onWrenchHarvest}.
     */
    private void removeSingleSign(Level level, BlockPos pos, BlockState state, SignPostSignFixtureBlockEntity be,
            Direction face, Player player) {
        ItemStack removed = be.removeFace(face);
        if (!player.isCreative())
            SignPostNetworkUtil.giveOrDrop(player, removed);

        BlockState newState = setConnectionStates(state, level, pos);
        level.setBlock(pos, newState, Block.UPDATE_CLIENTS);

        playRemoveItemSound(level, pos, player);
        level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);

        Constants.LOG.info("Removed sign from face {} of Sign Fixture at {}", face, pos);
    }

    // #region Sound

    private static void playAddItemSound(Level level, BlockPos pos, Player player) {
        level.playSound(null, pos, ModSoundEvents.SIGN_POST_SIGN_FIXTURE_ADD_ITEM.get(), SoundSource.PLAYERS, 1.0f,
                0.7f);
    }

    private static void playRemoveItemSound(Level level, BlockPos pos, Player player) {
        level.playSound(player, pos, ModSoundEvents.SIGN_POST_SIGN_FIXTURE_REMOVE_ITEM.get(), SoundSource.PLAYERS, 1.0f,
                0.7f);
    }

    // #region Drop contained signs on block removal

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && !movedByPiston
                && level.getBlockEntity(pos) instanceof SignPostSignFixtureBlockEntity be) {
            for (Direction face : Direction.values()) {
                SignFixtureFaceData data = be.getFaceData(face);
                if (data != null && !data.getItem().isEmpty())
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), data.getItem());
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    // #region IWrenchConfigurable - per-face Stance / Rotation / Scale /
    // Double-sided / Bright

    private static final int MODE_STANCE = 0;
    private static final int MODE_ROTATION = 1;
    private static final int MODE_SCALE = 2;
    private static final int MODE_OFFSET = 3;
    private static final int MODE_DOUBLE_SIDED = 4;
    private static final int MODE_BRIGHT = 5;
    private static final int MODE_COUNT = 6;

    private static SignSupportType getSupportType(SignFixtureFaceData data) {
        if (data.getItem().getItem() instanceof SignItem signItem)
            return signItem.getSignType().getSupportType();
        return SignSupportType.ANY;
    }

    /**
     * {@link com.drunkencod.symbols_n_signs.item.IWrenchConfigurable} doesn't pass
     * a BlockPos to this method, so it can't consult the BlockEntity to check
     * whether {@code clickedFace} is actually occupied. RatchetWrenchItem never
     * calls this method today (it dispatches straight to onWrenchLeftClick /
     * onWrenchRightClick, which do have a BlockPos and correctly no-op on
     * unoccupied faces), so this is a harmless upper-bound rather than a load-
     * bearing gate.
     */
    @Override
    public int getWrenchModeCount(BlockState state, Direction clickedFace) {
        return MODE_COUNT;
    }

    @Override
    public String getWrenchModeKey(BlockState state, Direction clickedFace, int modeIndex) {
        return switch (modeIndex) {
            case MODE_STANCE -> getModeKey("sign_fixture", "stance");
            case MODE_ROTATION -> getModeKey("sign_fixture", "rotation");
            case MODE_SCALE -> getModeKey("sign_fixture", "scale");
            case MODE_OFFSET -> getModeKey("sign_fixture", "offset");
            case MODE_DOUBLE_SIDED -> getModeKey("sign_fixture", "double_sided_mode");
            case MODE_BRIGHT -> getModeKey("sign_fixture", "bright");
            default -> Constants.MOD_ID + ".ratchet_wrench.mode.unknown";
        };
    }

    @Override
    public String getWrenchModeString(BlockState state, Direction clickedFace, int modeIndex) {
        return switch (modeIndex) {
            case MODE_STANCE -> getModeName("sign_fixture", "stance", modeIndex, MODE_COUNT);
            case MODE_ROTATION -> getModeName("sign_fixture", "rotation", modeIndex, MODE_COUNT);
            case MODE_SCALE -> getModeName("sign_fixture", "scale", modeIndex, MODE_COUNT);
            case MODE_OFFSET -> getModeName("sign_fixture", "offset", modeIndex, MODE_COUNT);
            case MODE_DOUBLE_SIDED -> getModeName("sign_fixture", "double_sided_mode", modeIndex, MODE_COUNT);
            case MODE_BRIGHT -> getModeName("sign_fixture", "bright", modeIndex, MODE_COUNT);
            default -> Constants.MOD_ID + ".ratchet_wrench.mode.unknown";
        };
    }

    @Override
    public Component getCurrentModeComponent(BlockState state, Direction clickedFace, Player player) {
        if (clickedFace == null)
            return Component.empty();
        ItemStack wrench = RatchetWrenchItem.getWrenchInHand(player);
        if (wrench.isEmpty())
            return Component.empty();
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(this);
        int mode = RatchetWrenchItem.getSelectedMode(wrench, blockId) % MODE_COUNT;
        return Component.literal(getWrenchModeString(state, clickedFace, mode));
    }

    @Override
    public InteractionResult onWrenchLeftClick(Level level, BlockPos pos, BlockState state, Direction clickedFace,
            @Nullable Vec3 hitLocation, Player player) {
        if (!(level.getBlockEntity(pos) instanceof SignPostSignFixtureBlockEntity be))
            return InteractionResult.PASS;
        Direction face = resolveTargetFace(pos, be, hitLocation, true);
        if (face == null)
            return InteractionResult.PASS;
        if (!level.isClientSide()) {
            ItemStack wrench = RatchetWrenchItem.getWrenchInHand(player);
            if (wrench.isEmpty())
                return InteractionResult.PASS;
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(this);
            int next = (RatchetWrenchItem.getSelectedMode(wrench, blockId) + (player.isShiftKeyDown() ? -1 : 1))
                    % MODE_COUNT;
            if (next <= -1)
                next = MODE_COUNT - 1;
            RatchetWrenchItem.setSelectedMode(wrench, blockId, next);
            player.displayClientMessage(Component.literal(getWrenchModeString(state, face, next)), true);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult onWrenchRightClick(Level level, BlockPos pos, BlockState state, Direction clickedFace,
            Vec3 hitLocation, Player player) {
        if (!(level.getBlockEntity(pos) instanceof SignPostSignFixtureBlockEntity be))
            return InteractionResult.PASS;
        Direction face = resolveTargetFace(pos, be, hitLocation, true);
        if (face == null)
            return InteractionResult.PASS;
        SignFixtureFaceData data = be.getFaceData(face);
        if (data == null)
            return InteractionResult.PASS;

        if (!level.isClientSide()) {
            ItemStack wrench = RatchetWrenchItem.getWrenchInHand(player);
            if (wrench.isEmpty())
                return InteractionResult.PASS;
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(this);
            int mode = RatchetWrenchItem.getSelectedMode(wrench, blockId);
            Constants.LOG.debug("Sign Fixture wrench right-click at {} face {} mode {}", pos, face, mode);
            switch (mode) {
                case MODE_STANCE -> {
                    List<SignStance> available = SignStance.getAvailableStances(getSupportType(data), face);
                    int idx = available.indexOf(data.getStance());
                    SignStance nextStance = available.get((idx + 1) % available.size());
                    be.setFaceData(face, data.withStance(nextStance));
                    player.displayClientMessage(
                            Component.translatable(getWrenchModeKey(state, face, MODE_STANCE))
                                    .append(": ")
                                    .append(Component.translatable(
                                            getWrenchModeKey(state, clickedFace, MODE_STANCE) + ".value."
                                                    + nextStance.getName())),
                            true);
                }
                case MODE_ROTATION -> {
                    int nextRot = (data.getRotation() + 1) % SignFixtureFaceData.ROTATION_COUNT;
                    be.setFaceData(face, data.withRotation(nextRot));
                    player.displayClientMessage(
                            Component.translatable(getWrenchModeKey(state, face, MODE_ROTATION))
                                    .append(": " + nextRot),
                            true);
                }
                case MODE_SCALE -> {
                    float nextSc = Math.round((data.getScale() + SignFixtureFaceData.SCALE_STEP) * 1000f) / 1000f;
                    if (nextSc > SignFixtureFaceData.MAX_SCALE + 1e-3f)
                        nextSc = SignFixtureFaceData.MIN_SCALE;
                    be.setFaceData(face, data.withScale(nextSc));
                    player.displayClientMessage(
                            Component.translatable(getWrenchModeKey(state, face, MODE_SCALE))
                                    .append(": " + nextSc),
                            true);
                }
                case MODE_OFFSET -> {
                    float nextOff = Math.round((data.getOffset() + SignFixtureFaceData.OFFSET_STEP) * 1000f) / 1000f;
                    if (nextOff > SignFixtureFaceData.MAX_OFFSET + 1e-3f)
                        nextOff = SignFixtureFaceData.MIN_OFFSET;
                    be.setFaceData(face, data.withOffset(nextOff));
                    player.displayClientMessage(
                            Component.translatable(getWrenchModeKey(state, face, MODE_OFFSET))
                                    .append(": " + nextOff),
                            true);
                }
                case MODE_DOUBLE_SIDED -> {
                    int nextDblSided = (data.getDoubleSidedMode() + 1) % SignFixtureFaceData.DOUBLE_SIDED_MODE_COUNT;
                    be.setFaceData(face, data.withDoubleSided(nextDblSided));
                    player.displayClientMessage(
                            Component.translatable(getWrenchModeKey(state, face, MODE_DOUBLE_SIDED))
                                    .append(": ")
                                    .append(Component.translatable(
                                            getWrenchModeKey(state, clickedFace, MODE_DOUBLE_SIDED) + ".value."
                                                    + nextDblSided)),
                            true);
                }
                case MODE_BRIGHT -> {
                    boolean nextBright = !data.isBright();
                    be.setFaceData(face, data.withBright(nextBright));
                    player.displayClientMessage(
                            Component.translatable(getWrenchModeKey(state, face, MODE_BRIGHT))
                                    .append(": ")
                                    .append(Component.translatable(
                                            getWrenchModeKey(state, clickedFace, MODE_BRIGHT) + ".value."
                                                    + nextBright)),
                            true);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    // #region Wrench sneak right-click harvest

    /**
     * Unlike single-item fixtures, a Sign Fixture can hold up to 6 independent
     * sign items, each with its own NBT (custom name, dye, glow, etc.) that no
     * shared loot table entry could reproduce. So instead of the default
     * "drop the whole block's loot" behavior, this resolves which occupied
     * face's pane was actually hit (no nearest-face fallback - landing outside
     * every pane means the post itself was targeted):
     * <ul>
     * <li>a specific sign's pane was hit -&gt; remove just that sign</li>
     * <li>the post was hit (or there's no BE) -&gt; drop every sign and revert
     * to a plain Sign Post</li>
     * </ul>
     */
    @Override
    public void onWrenchHarvest(Level level, BlockPos pos, BlockState state, @Nullable Vec3 hitLocation,
            ItemStack wrenchStack, Player player) {
        if (!(level.getBlockEntity(pos) instanceof SignPostSignFixtureBlockEntity be)) {
            super.onWrenchHarvest(level, pos, state, hitLocation, wrenchStack, player);
            return;
        }

        Direction face = resolveTargetFace(pos, be, hitLocation, false);
        if (face != null && be.isOccupied(face)) {
            removeSingleSign(level, pos, state, be, face, player);
            return;
        }

        // Drain every face before reverting: onRemove() also drops contained signs
        // when this block is replaced by a different block (see onRemove below), so
        // any face still present here would otherwise be handed to the player twice.
        for (Direction occupied : Set.copyOf(be.getOccupiedFaces())) {
            ItemStack removed = be.removeFace(occupied);
            if (!player.isCreative())
                SignPostNetworkUtil.giveOrDrop(player, removed);
        }
        if (!player.isCreative())
            SignPostNetworkUtil.giveOrDrop(player, new ItemStack(this));
        revertToPost(level, pos, state, player);
    }

    // #region BlockEntity

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SignPostSignFixtureBlockEntity(pos, state);
    }

    @Override
    protected MapCodec<SignPostSignFixtureBlock> codec() {
        return CODEC;
    }
}
