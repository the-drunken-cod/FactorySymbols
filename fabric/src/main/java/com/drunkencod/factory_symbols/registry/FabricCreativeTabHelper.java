package com.drunkencod.factory_symbols.registry;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class FabricCreativeTabHelper implements ICreativeTabHelper {

    @Override
    public void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, Constants.MOD_ID),
                FabricItemGroup.builder()
                        .title(Component.translatable("itemGroup." + Constants.MOD_ID))
                        .icon(() -> new ItemStack(ModItems.TEMPLATES.get(SymbolMaterial.PAPER).get()))
                        .displayItems((params, output) -> ModItems.populateCreativeTab(output))
                        .build());
    }
}
