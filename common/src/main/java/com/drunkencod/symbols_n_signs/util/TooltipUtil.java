package com.drunkencod.symbols_n_signs.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.locale.Language;
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
     * Resolves {@code key} and splits it into one tooltip line per literal
     * {@code \n} in the translated text, so translators can add or remove
     * tooltip lines purely by editing the language json. Legacy formatting
     * codes (e.g. {@code §c}) embedded in the translation are rendered
     * natively, so colors can be added the same way. Does not support
     * {@code %s}-style format arguments; use
     * {@link #tooltipLine(String, ChatFormatting...)} with
     * {@link Component#translatable(String, Object...)} for those.
     */
    public static List<Component> tooltipLines(String key, ChatFormatting... styles) {
        String raw = Language.getInstance().getOrDefault(key);
        String[] rawLines = raw.split("\n", -1);
        List<Component> lines = new ArrayList<>(rawLines.length);
        for (String rawLine : rawLines)
            lines.add(Component.literal(rawLine).withStyle(styles));
        return lines;
    }

    /**
     * Like {@link #tooltipLines(String, ChatFormatting...)}, styled gray.
     */
    public static List<Component> tooltipLines(String key) {
        return tooltipLines(key, ChatFormatting.GRAY);
    }

    /**
     * Concatenates the given tooltip line groups into a single list, for
     * items whose expanded tooltip is made up of several translation keys.
     */
    @SafeVarargs
    public static List<Component> combine(List<Component>... lineGroups) {
        List<Component> result = new ArrayList<>();
        for (List<Component> group : lineGroups)
            result.addAll(group);
        return result;
    }

    /**
     * If given the registry name of an item, returns its expanded tooltip lines.
     */
    public static List<Component> getItemTooltip(String itemRegistryName) {
        return tooltipLines("item.symbols_n_signs." + itemRegistryName + ".tooltip");
    }

    /**
     * If given the registry name of a block, returns its expanded tooltip lines.
     */
    public static List<Component> getBlockTooltip(String itemRegistryName) {
        return tooltipLines("block.symbols_n_signs." + itemRegistryName + ".tooltip");
    }
}
