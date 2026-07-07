package com.drunkencod.symbols_n_signs.item;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * Implemented by items whose full tooltip should only be shown while the
 * player holds shift. When shift is not held, a "hold shift" hint is shown
 * instead.
 */
public interface IExpandableTooltip {
    List<Component> getExpandedTooltip(ItemStack stack);
}
