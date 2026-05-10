package com.drunkencod.factory_symbols.registry;

import com.drunkencod.factory_symbols.block.display_panel.DisplayPanelBlock;
import com.drunkencod.factory_symbols.item.SymbolItem;
import com.drunkencod.factory_symbols.platform.Services;
import com.drunkencod.factory_symbols.symbols.SymbolCategory;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModItems {

    // #region Symbol items — one item per material × symbol
    public static final Map<SymbolMaterial, Map<SymbolType, Supplier<Item>>> SYMBOLS = new EnumMap<>(
            SymbolMaterial.class);

    public static void register() {
        for (SymbolMaterial mat : SymbolMaterial.values()) {
            Map<SymbolType, Supplier<Item>> symMap = new EnumMap<>(SymbolType.class);
            for (SymbolType sym : SymbolType.values()) {
                Supplier<Item> item = Services.REGISTRY.registerItem(
                        sym.getId() + "_" + mat.getPrefix(),
                        () -> new SymbolItem(mat, sym, new Item.Properties()));
                symMap.put(sym, item);
            }
            SYMBOLS.put(mat, symMap);
        }
    }

    public static ItemStack getSymbolStack(SymbolMaterial mat, SymbolType sym) {
        return SYMBOLS.get(mat).get(sym).get().getDefaultInstance();
    }

    // #region Creative tab
    public static void populateCreativeTab(CreativeModeTab.Output output) {
        // display panel colors:
        for (int i : DisplayPanelBlock.COLORS_ORDERED) {
            ItemStack itm = ModBlocks.DISPLAY_PANEL_ITEM.get().getDefaultInstance();
            itm.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(i));
            output.accept(itm);
        }

        // symbols — grouped by category within each material:
        for (SymbolMaterial mat : SymbolMaterial.values())
            for (SymbolCategory cat : SymbolCategory.values())
                for (SymbolType sym : SymbolType.values())
                    if (sym.getCategory() == cat)
                        output.accept(getSymbolStack(mat, sym));
    }
}
