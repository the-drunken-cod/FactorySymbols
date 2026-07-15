package com.drunkencod.symbols_n_signs.client;

import java.util.List;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.item.IExpandableTooltip;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * Cross-loader tooltip logic for {@link IExpandableTooltip} items, wired up
 * from each loader's client-side tooltip event/callback.
 */
public final class ExpandableTooltipHandler {

    private ExpandableTooltipHandler() {
    }

    /**
     * Index right after the item name line (index 0), where the expandable
     * tooltip content is inserted. This runs after the tooltip is fully
     * assembled, so inserting here puts our content before the Ctrl+F3+H
     * registry id line and the blue mod name line added at the end.
     */
    private static final int INSERT_INDEX = 1;

    public static void append(ItemStack stack, List<Component> tooltip) {
        if (!(stack.getItem() instanceof IExpandableTooltip expandable))
            return;

        List<Component> tooltipComponents = expandable.getExpandedTooltip(stack);

        if (tooltipComponents.size() > 0) {
            int index = Math.min(INSERT_INDEX, tooltip.size());

            if (Screen.hasShiftDown())
                tooltip.addAll(index, tooltipComponents);
            else {
                tooltip.add(index, Component.translatable("tooltip." + Constants.MOD_ID + ".hold_shift")
                        .withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
