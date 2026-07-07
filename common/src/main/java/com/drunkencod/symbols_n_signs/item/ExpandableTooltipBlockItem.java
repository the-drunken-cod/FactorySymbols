package com.drunkencod.symbols_n_signs.item;

import java.util.List;

import com.drunkencod.symbols_n_signs.util.TooltipUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/**
 * A {@link BlockItem} with a single-line tooltip that only expands while
 * shift is held, via {@link IExpandableTooltip}.
 */
public class ExpandableTooltipBlockItem extends BlockItem implements IExpandableTooltip {
    private final String tooltipKey;

    public ExpandableTooltipBlockItem(Block block, Item.Properties properties, String tooltipKey) {
        super(block, properties);
        this.tooltipKey = tooltipKey;
    }

    @Override
    public List<Component> getExpandedTooltip(ItemStack stack) {
        return List.of(TooltipUtil.tooltipLine(tooltipKey));
    }
}
