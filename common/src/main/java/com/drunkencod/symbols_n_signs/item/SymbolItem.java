package com.drunkencod.symbols_n_signs.item;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.symbols.SymbolMaterial;
import com.drunkencod.symbols_n_signs.symbols.SymbolType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SymbolItem extends Item {

    private final SymbolMaterial material;
    private final SymbolType symbolType;

    public SymbolItem(SymbolMaterial material, SymbolType symbolType, Properties properties) {
        super(properties);
        this.material = material;
        this.symbolType = symbolType;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(
                "symbols_n_signs.symbol_item_name_template",
                Component.translatable("material." + Constants.MOD_ID + "." + material.getPrefix()),
                Component.translatable("symbol." + Constants.MOD_ID + "." + symbolType.getId()));
    }
}
