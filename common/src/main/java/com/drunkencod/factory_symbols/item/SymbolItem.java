package com.drunkencod.factory_symbols.item;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;

public class SymbolItem extends Item {

    private final SymbolMaterial material;

    public SymbolItem(SymbolMaterial material, Properties properties) {
        super(properties);
        this.material = material;
    }

    @Override
    public Component getName(ItemStack stack) {
        int ordinal = stack.getOrDefault(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.DEFAULT).value();
        SymbolType[] types = SymbolType.values();
        SymbolType sym = (ordinal >= 0 && ordinal < types.length) ? types[ordinal] : types[0];
        return Component.translatable(
                "factory_symbols.item_name_template",
                Component.translatable("material." + Constants.MOD_ID + "." + material.getPrefix()),
                Component.translatable("symbol." + Constants.MOD_ID + "." + sym.getId()));
    }
}
