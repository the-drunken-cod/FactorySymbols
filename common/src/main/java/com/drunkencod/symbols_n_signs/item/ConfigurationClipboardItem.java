package com.drunkencod.symbols_n_signs.item;

import java.util.List;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.util.TooltipUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

// TODO:
// - [ ] shift-right-click on block matching `#symbols_n_signs:configurable` copies the configuration
//     - configuration is copied into `custom_data`, indexed by the registry id of the block
//     - each block in `#symbols_n_signs:configurable` needs to implement `IWrenchConfigurable`
//         - `IWrenchConfigurable` needs to be expanded by a method that returns the NBT keys to store, as well as a static constant format version int `IWrenchConfigurable.FORMAT_VERSION = 1` (migration unimplemented for now)
//     - show client message (e.g. "%1$s configuration saved to clipboard.")
// - [ ] right-click on block matching `#symbols_n_signs:configurable` pastes the configuration
//     - if no `custom_data.<block_registry_id>` object is found, show a client message (e.g. "No configuration found for %1$s" in red)
//     - if `custom_data.<block_registry_id>` exists and format version matches, copy configuration over to block NBT
// - [ ] when held in the offhand when placing a block matching `#symbols_n_signs:configurable`, override it with the previously copied NBT, if there is any
//     - show client message (e.g. "Placed %1$s with clipboard's configuration")

public class ConfigurationClipboardItem extends Item implements IExpandableTooltip {

    public static final String ID = "configuration_clipboard";

    public ConfigurationClipboardItem(Properties properties) {
        super(properties);
    }

    @Override
    public List<Component> getExpandedTooltip(ItemStack stack) {
        return List.of(
                TooltipUtil.tooltipLine("item." + Constants.MOD_ID + ".configuration_clipboard.tooltip.1"),
                TooltipUtil.tooltipLine("item." + Constants.MOD_ID + ".configuration_clipboard.tooltip.2"),
                TooltipUtil.tooltipLine("item." + Constants.MOD_ID + ".configuration_clipboard.tooltip.3"));
    }
}
