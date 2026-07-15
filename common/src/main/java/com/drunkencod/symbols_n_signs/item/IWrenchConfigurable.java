package com.drunkencod.symbols_n_signs.item;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

/**
 * Implemented by blocks that can be configured with the Ratchet Wrench.
 * Left-click cycles the selected mode; right-click cycles the current mode's
 * value.
 *
 * Modes are identified by zero-based index. The translation key convention is:
 * {@code symbols_n_signs.ratchet_wrench.mode.<fixture_id>.<mode_name>} for the
 * mode label, and
 * {@code symbols_n_signs.ratchet_wrench.mode.<fixture_id>.<mode_name>.value.<n>}
 * for individual values.
 *
 * Every method receives the {@link Direction} of the block's outer face that
 * was hit. Single-face fixtures (Button/Lamp/Redstone Emitter) use it
 * directly. Multi-face fixtures (Sign Fixture) can't rely on it alone — a
 * sign's rendered pane often doesn't lie flush with the post's outer face
 * (e.g. a perpendicular Stance, or the backside of a double-sided sign), so
 * {@link #onWrenchLeftClick} / {@link #onWrenchRightClick} additionally
 * receive the exact world-space {@code hitLocation}, which they use to test
 * against each occupied face's actual collision sub-shape instead.
 */
public interface IWrenchConfigurable {

    /** Returns how many modes this block exposes to the wrench. */
    int getWrenchModeCount(BlockState state, Direction clickedFace);

    /**
     * Returns the translation key prefix for the mode at the given index,
     * e.g.
     * {@code "symbols_n_signs.ratchet_wrench.mode.button_fixture.orientation"}.
     */
    String getWrenchModeKey(BlockState state, Direction clickedFace, int modeIndex);

    /**
     * Returns the translation for the mode at the given index, e.g.
     * {@code "symbols_n_signs.ratchet_wrench.mode.button_fixture.orientation"}.
     */
    String getWrenchModeString(BlockState state, Direction clickedFace, int modeIndex);

    /**
     * Returns a display component describing the current value of the selected
     * mode.
     * Used for the hotbar overlay message when switching modes.
     */
    Component getCurrentModeComponent(BlockState state, Direction clickedFace, Player player);

    /**
     * Called when the player left-clicks a block with the Ratchet Wrench.
     * Should cycle the selected mode (stored in the wrench item's NBT).
     *
     * @param hitLocation exact world-space point that was hit, or {@code null}
     *                     if it couldn't be recovered (left-click events don't
     *                     carry it natively; the caller re-raycasts to find it)
     * @return {@link InteractionResult#SUCCESS} if the mode was changed
     */
    InteractionResult onWrenchLeftClick(Level level, BlockPos pos, BlockState state, Direction clickedFace,
            @Nullable Vec3 hitLocation, Player player);

    /**
     * Called when the player right-clicks a block with the Ratchet Wrench.
     * Should cycle the value of the currently selected mode.
     *
     * @param hitLocation exact world-space point that was hit
     * @return {@link InteractionResult#SUCCESS} if the value was changed
     */
    InteractionResult onWrenchRightClick(Level level, BlockPos pos, BlockState state, Direction clickedFace,
            Vec3 hitLocation, Player player);

    /**
     * Format version of this block's Configuration (see {@link #copyConfiguration}).
     * Each implementor defines its own version; bumping it invalidates only that
     * block type's previously-stored Configuration Clipboard entries, not any
     * other block's. Migration between versions is not implemented - a clipboard
     * entry saved under a different version is treated the same as no entry at
     * all.
     */
    int getConfigFormatVersion();

    /**
     * Snapshots this block's Configuration - the wrench-configurable subset of
     * its state, excluding transient/derived state (e.g. a Button's Pressed
     * flag) - into a fresh {@link CompoundTag} for the Configuration Clipboard.
     * <p>
     * May include an item this block holds (e.g. a Sign Fixture face, a Display
     * Panel's stored item). Implementors must never let paste duplicate that
     * item - see {@link ConfigurationClipboardUtil#takeMatchingItem} for how
     * paste re-resolves it from the pasting player's own inventory instead.
     *
     * @param hitLocation exact world-space point that was clicked, or
     *                     {@code null} if there wasn't a click (e.g. copying via
     *                     the offhand-on-place path never applies to copy, but
     *                     multi-face blocks like the Sign Fixture need this to
     *                     resolve which of their occupied sub-shapes to copy)
     */
    CompoundTag copyConfiguration(Level level, BlockPos pos, BlockState state, Direction clickedFace,
            @Nullable Vec3 hitLocation, Player player);

    /**
     * Applies a previously-copied Configuration to this block at the given
     * position (and, for multi-face blocks, whichever face {@code hitLocation}
     * resolves to).
     *
     * @param hitLocation exact world-space point that was clicked, or
     *                     {@code null} when applied via the offhand-on-place
     *                     path (no click occurred). Multi-face blocks (Sign
     *                     Fixture) have no meaningful target face in that case
     *                     and should simply no-op.
     * @return {@code true} if the paste changed anything, used by the caller to
     *         decide whether to show a success message
     */
    boolean pasteConfiguration(Level level, BlockPos pos, BlockState state, Direction clickedFace,
            @Nullable Vec3 hitLocation, CompoundTag data, Player player);

    /**
     * Called when the player sneak-right-clicks this block with the Ratchet
     * Wrench instead of cycling a mode value - harvests the block instantly
     * instead of requiring it to be mined. Default behavior gives back this
     * block's own loot-table drops and destroys it. Implementors holding extra
     * items of their own (e.g. a Display Panel's stored item) override this to
     * hand those back too and to prevent them from also being dropped by
     * {@code onRemove}. In creative mode nothing is given or dropped.
     */
    default void onWrenchHarvest(Level level, BlockPos pos, BlockState state, @Nullable Vec3 hitLocation,
            ItemStack wrenchStack, Player player) {
        if (!player.isCreative()) {
            BlockEntity be = level.getBlockEntity(pos);
            List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, pos, be, player, wrenchStack);
            for (ItemStack drop : drops)
                giveOrDrop(player, drop);
        }
        level.destroyBlock(pos, false);
    }

    /**
     * Adds {@code stack} to the player's inventory, dropping any overflow at
     * their feet instead of letting it vanish.
     */
    static void giveOrDrop(Player player, ItemStack stack) {
        if (!stack.isEmpty() && !player.getInventory().add(stack))
            player.drop(stack, false);
    }
}
