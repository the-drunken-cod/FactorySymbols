package com.drunkencod.factory_symbols.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemLore;

public class RatchetWrenchItem extends Item {
    public static final String ID = "ratchet_wrench";

    public RatchetWrenchItem(Properties properties) {
        super(properties
                .component(DataComponents.LORE, new ItemLore(
                        List.of(
                                (Component.empty()
                                        .append(Component.translatable("item.factory_symbols.ratchet_wrench.tooltip.1"))
                                        .withStyle(ChatFormatting.GRAY)),
                                (Component.empty()
                                        .append(Component.translatable("item.factory_symbols.ratchet_wrench.tooltip.2"))
                                        .withStyle(ChatFormatting.GRAY))))));
    }
}
