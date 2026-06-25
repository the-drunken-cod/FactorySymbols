package com.drunkencod.symbols_n_signs.registry;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.signs.SignType;
import com.drunkencod.symbols_n_signs.symbols.SymbolMaterial;
import com.drunkencod.symbols_n_signs.symbols.SymbolType;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class FabricCreativeTabHelper implements ICreativeTabHelper {

    @Override
    public void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID,
                        ICreativeTabHelper.TAB_MATERIALS_AND_BLOCKS_KEY),
                FabricItemGroup.builder()
                        .title(Component.translatable(ICreativeTabHelper.TAB_MATERIALS_AND_BLOCKS_TR_KEY))
                        .icon(() -> ModBlocks.SIGN_POST_ITEM.get().getDefaultInstance())
                        .displayItems((params, output) -> ModItems.populateMaterialsAndBlocksTab(output))
                        .build());
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, ICreativeTabHelper.TAB_SYMBOLS_KEY),
                FabricItemGroup.builder()
                        .title(Component.translatable(ICreativeTabHelper.TAB_SYMBOLS_TR_KEY))
                        .icon(() -> ModItems.getSymbolStack(SymbolMaterial.IRON,
                                SymbolType.LETTER_A))
                        .displayItems((params, output) -> ModItems.populateSymbolsTab(output))
                        .build());
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, ICreativeTabHelper.TAB_SIGNS_KEY),
                FabricItemGroup.builder()
                        .title(Component.translatable(ICreativeTabHelper.TAB_SIGNS_TR_KEY))
                        .icon(() -> ModItems.getSignStack(SignType.REGULATORY_GIVE_WAY))
                        .displayItems((params, output) -> ModItems.populateSignsTab(output))
                        .build());
    }
}
