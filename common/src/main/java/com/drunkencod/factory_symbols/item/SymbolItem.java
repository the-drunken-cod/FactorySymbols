package com.drunkencod.factory_symbols.item;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;
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
                "factory_symbols.item_name_template",
                Component.translatable("material." + Constants.MOD_ID + "." + material.getPrefix()),
                Component.translatable("symbol." + Constants.MOD_ID + "." + symbolType.getId()));
    }
}
