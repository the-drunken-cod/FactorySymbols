package com.drunkencod.factory_symbols.block.display_panel;

import com.drunkencod.factory_symbols.util.TooltipUtil;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.level.block.Block;

public class DisplayPanelItem extends BlockItem {

    public DisplayPanelItem(Block block, Item.Properties properties) {
        super(block, properties.component(DataComponents.LORE,
                TooltipUtil.getTooltip("block.factory_symbols.display_panel.tooltip")));
    }

    @Override
    public Component getName(ItemStack stack) {
        CustomModelData cmd = stack.get(DataComponents.CUSTOM_MODEL_DATA);
        int id = cmd != null ? cmd.value() : 0;
        DyeColor color = DisplayPanelBlock.modelIdToColor(id);
        int textCol = color == DyeColor.BLACK ? 0xFF555555 : color.getTextColor();
        return Component.translatable("color.minecraft." + color.getName())
                .withColor(textCol)
                .append(" ")
                .append(super.getName(stack));
    }
}
