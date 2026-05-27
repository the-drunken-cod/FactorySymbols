package com.drunkencod.factory_symbols.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Implemented by blocks that can be configured with the Ratchet Wrench.
 * Left-click cycles the selected mode; right-click cycles the current mode's
 * value.
 *
 * Modes are identified by zero-based index. The translation key convention is:
 * {@code factory_symbols.ratchet_wrench.mode.<fixture_id>.<mode_name>} for the
 * mode label, and
 * {@code factory_symbols.ratchet_wrench.mode.<fixture_id>.<mode_name>.value.<n>}
 * for individual values.
 */
public interface IWrenchConfigurable {

    /** Returns how many modes this block exposes to the wrench. */
    int getWrenchModeCount(BlockState state);

    /**
     * Returns the translation key prefix for the mode at the given index,
     * e.g.
     * {@code "factory_symbols.ratchet_wrench.mode.button_fixture.orientation"}.
     */
    String getWrenchModeKey(BlockState state, int modeIndex);

    /**
     * Returns the translation for the mode at the given index, e.g.
     * {@code "factory_symbols.ratchet_wrench.mode.button_fixture.orientation"}.
     */
    String getWrenchModeString(BlockState state, int modeIndex);

    /**
     * Returns a display component describing the current value of the selected
     * mode.
     * Used for the hotbar overlay message when switching modes.
     */
    Component getCurrentModeComponent(BlockState state, Player player);

    /**
     * Called when the player left-clicks a block with the Ratchet Wrench.
     * Should cycle the selected mode (stored in the wrench item's NBT).
     *
     * @return {@link InteractionResult#SUCCESS} if the mode was changed
     */
    InteractionResult onWrenchLeftClick(Level level, BlockPos pos, BlockState state, Player player);

    /**
     * Called when the player right-clicks a block with the Ratchet Wrench.
     * Should cycle the value of the currently selected mode.
     *
     * @return {@link InteractionResult#SUCCESS} if the value was changed
     */
    InteractionResult onWrenchRightClick(Level level, BlockPos pos, BlockState state, Player player);
}
