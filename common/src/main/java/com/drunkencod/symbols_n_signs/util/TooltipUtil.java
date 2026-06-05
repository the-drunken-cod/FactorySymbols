package com.drunkencod.symbols_n_signs.util;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.ItemLore;

public class TooltipUtil {
    /**
     * Returns a tooltip {@link ItemLore} component for the given translation key.
     */
    public static ItemLore getTooltip(String key, ChatFormatting... styles) {
        return new ItemLore(
                List.of((Component.empty().append(Component.translatable(key)).withStyle(styles))));
    }

    /**
     * Returns a tooltip {@link ItemLore} component for the given translation key.
     */
    public static ItemLore getTooltip(String key) {
        return getTooltip(key, ChatFormatting.GRAY);
    }

    /**
     * If given the registry name of an item, returns its tooltip {@link ItemLore}
     * component.
     */
    public static ItemLore getItemTooltip(String itemRegistryName) {
        return getTooltip("item.symbols_n_signs." + itemRegistryName + ".tooltip");
    }

    /**
     * If given the registry name of a block, returns its tooltip {@link ItemLore}
     * component.
     */
    public static ItemLore getBlockTooltip(String itemRegistryName) {
        return getTooltip("block.symbols_n_signs." + itemRegistryName + ".tooltip");
    }
}
