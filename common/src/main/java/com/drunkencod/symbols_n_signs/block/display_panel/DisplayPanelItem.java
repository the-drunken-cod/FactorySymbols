package com.drunkencod.symbols_n_signs.block.display_panel;

import java.util.List;

import com.drunkencod.symbols_n_signs.item.IExpandableTooltip;
import com.drunkencod.symbols_n_signs.util.TooltipUtil;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.level.block.Block;

public class DisplayPanelItem extends BlockItem implements IExpandableTooltip {

    public DisplayPanelItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public List<Component> getExpandedTooltip(ItemStack stack) {
        return List.of(TooltipUtil.tooltipLine("block.symbols_n_signs.display_panel.tooltip"));
    }

    @Override
    public Component getName(ItemStack stack) {
        CustomModelData cmd = stack.get(DataComponents.CUSTOM_MODEL_DATA);
        int id = cmd != null ? cmd.value() : 0;
        DyeColor color = DisplayPanelBlock.modelIdToColor(id);
        int textCol = color == DyeColor.BLACK ? 0xFF555555 : color.getTextColor();
        return Component.translatable("block.symbols_n_signs.display_panel." + color.getName()).withColor(textCol);
    }
}
