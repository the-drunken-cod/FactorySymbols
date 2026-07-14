package com.drunkencod.symbols_n_signs.item;

import com.drunkencod.symbols_n_signs.Constants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

/**
 * Shared logic for the Configuration Clipboard, used both by
 * {@link ConfigurationClipboardItem} (shift-right-click/right-click on a
 * block) and by every {@code IWrenchConfigurable} block's {@code setPlacedBy}
 * (offhand-clipboard-on-place). See ADR 0003/0004 and {@code CONTEXT.md} for
 * the design rationale.
 */
public final class ConfigurationClipboardUtil {

    private static final String NBT_FORMAT_VERSION = "format_version";
    private static final String NBT_DATA = "data";

    private ConfigurationClipboardUtil() {
    }

    // #region Clipboard storage (custom_data on the clipboard stack, keyed by block registry id)

    public static void storeConfiguration(ItemStack clipboard, ResourceLocation blockId, int formatVersion,
            CompoundTag data) {
        CompoundTag root = clipboard.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        CompoundTag entry = new CompoundTag();
        entry.putInt(NBT_FORMAT_VERSION, formatVersion);
        entry.put(NBT_DATA, data);
        root.put(blockId.toString(), entry);
        clipboard.set(DataComponents.CUSTOM_DATA, CustomData.of(root));
    }

    /**
     * Returns the stored Configuration for the given block, or {@code null} if
     * none is stored, or its Format Version doesn't match {@code
     * expectedFormatVersion} (treated identically to nothing stored - see
     * {@link IWrenchConfigurable#getConfigFormatVersion}).
     */
    public static @Nullable CompoundTag getConfiguration(ItemStack clipboard, ResourceLocation blockId,
            int expectedFormatVersion) {
        CompoundTag root = clipboard.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        String key = blockId.toString();
        if (!root.contains(key))
            return null;
        CompoundTag entry = root.getCompound(key);
        if (entry.getInt(NBT_FORMAT_VERSION) != expectedFormatVersion)
            return null;
        return entry.getCompound(NBT_DATA);
    }

    /** Number of block types this clipboard currently has a stored Configuration for. */
    public static int countStoredConfigurations(ItemStack clipboard) {
        CompoundTag root = clipboard.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return root.size();
    }

    // #region Inventory item matching (never duplicates, swaps, or discards an item)

    /**
     * Searches the player's main inventory and hotbar for a stack that's an
     * exact match (same item, same components) for {@code template}, removes
     * exactly 1 from it, and returns a single-count copy. Returns
     * {@link ItemStack#EMPTY} if {@code template} is empty or no match is
     * found. Never touches armor/offhand slots, and never creates or destroys
     * an item overall - only moves one the player already owns (see ADR 0004).
     */
    public static ItemStack takeMatchingItem(Player player, ItemStack template) {
        if (template.isEmpty())
            return ItemStack.EMPTY;
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack candidate = inventory.getItem(i);
            if (!candidate.isEmpty() && ItemStack.isSameItemSameComponents(candidate, template))
                return candidate.split(1);
        }
        return ItemStack.EMPTY;
    }

    // #region Offhand-on-place override

    /**
     * Called from every {@code IWrenchConfigurable} block's {@code
     * setPlacedBy}. If the placer holds a Configuration Clipboard in their
     * offhand with a stored, Format-Version-matching Configuration for this
     * block, applies it immediately - taking the place of a separate Ratchet
     * Wrench/Configuration Clipboard visit - and shows a confirmation message.
     */
    public static void applyOffhandConfigOnPlace(Level level, BlockPos pos, BlockState state,
            @Nullable LivingEntity placer) {
        if (level.isClientSide() || !(placer instanceof Player player))
            return;
        if (!(state.getBlock() instanceof IWrenchConfigurable configurable))
            return;
        ItemStack offhand = player.getOffhandItem();
        if (!(offhand.getItem() instanceof ConfigurationClipboardItem))
            return;

        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        CompoundTag data = getConfiguration(offhand, blockId, configurable.getConfigFormatVersion());
        if (data == null)
            return;

        BlockState liveState = level.getBlockState(pos);
        boolean changed = configurable.pasteConfiguration(level, pos, liveState, null, null, data, player);
        if (changed)
            player.displayClientMessage(
                    Component.translatable(
                            "item." + Constants.MOD_ID + ".configuration_clipboard.message.placed_with_configuration",
                            state.getBlock().getName()),
                    true);
    }
}
