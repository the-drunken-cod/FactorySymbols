package com.drunkencod.factory_symbols.registry;

import com.drunkencod.factory_symbols.platform.Services;
import com.drunkencod.factory_symbols.symbols.SymbolCategory;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModItems {

    // #region Template items — one per material
    public static final Map<SymbolMaterial, Supplier<Item>> TEMPLATES = new EnumMap<>(SymbolMaterial.class);

    // #region Symbol items — (material × symbol)
    public static final Map<SymbolMaterial, Map<SymbolType, Supplier<Item>>> SYMBOLS = new EnumMap<>(
            SymbolMaterial.class);

    public static void register() {
        for (SymbolMaterial mat : SymbolMaterial.values()) {
            TEMPLATES.put(mat, Services.REGISTRY.registerItem(
                    "template_" + mat.getPrefix(),
                    () -> new Item(new Item.Properties())));

            Map<SymbolType, Supplier<Item>> matSymbols = new EnumMap<>(SymbolType.class);
            for (SymbolType sym : SymbolType.values()) {
                matSymbols.put(sym, Services.REGISTRY.registerItem(
                        "symbol_" + mat.getPrefix() + "_" + sym.getId(),
                        () -> new Item(new Item.Properties())));
            }
            SYMBOLS.put(mat, Collections.unmodifiableMap(matSymbols));
        }
    }

    // #region Creative tab
    public static void populateCreativeTab(CreativeModeTab.Output output) {
        for (SymbolMaterial mat : SymbolMaterial.values()) {
            output.accept(TEMPLATES.get(mat).get());
            for (SymbolCategory cat : SymbolCategory.values()) {
                for (SymbolType sym : SymbolType.values()) {
                    if (sym.getCategory() == cat) {
                        output.accept(SYMBOLS.get(mat).get(sym).get());
                    }
                }
            }
        }
    }
}
