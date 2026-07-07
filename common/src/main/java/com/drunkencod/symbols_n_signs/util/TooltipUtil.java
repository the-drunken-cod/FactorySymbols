package com.drunkencod.symbols_n_signs.util;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class TooltipUtil {
    /**
     * Returns a single translated, styled tooltip line.
     */
    public static Component tooltipLine(String key, ChatFormatting... styles) {
        return Component.empty().append(Component.translatable(key)).withStyle(styles);
    }

    /**
     * Returns a single translated tooltip line, styled gray.
     */
    public static Component tooltipLine(String key) {
        return tooltipLine(key, ChatFormatting.GRAY);
    }

    /**
     * If given the registry name of an item, returns its expanded tooltip lines.
     */
    public static List<Component> getItemTooltip(String itemRegistryName) {
        return List.of(tooltipLine("item.symbols_n_signs." + itemRegistryName + ".tooltip"));
    }

    /**
     * If given the registry name of a block, returns its expanded tooltip lines.
     */
    public static List<Component> getBlockTooltip(String itemRegistryName) {
        return List.of(tooltipLine("block.symbols_n_signs." + itemRegistryName + ".tooltip"));
    }
}
