package com.drunkencod.symbols_n_signs.registry;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.signs.SignType;
import com.drunkencod.symbols_n_signs.symbols.SymbolMaterial;
import com.drunkencod.symbols_n_signs.symbols.SymbolType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NeoForgeCreativeTabHelper implements ICreativeTabHelper {

        private final DeferredRegister<CreativeModeTab> creativeTabs = DeferredRegister.create(
                        Registries.CREATIVE_MODE_TAB,
                        Constants.MOD_ID);

        public NeoForgeCreativeTabHelper() {
                creativeTabs.register(Constants.MOD_ID + "_blocks", () -> CreativeModeTab.builder()
                                .title(Component.translatable("itemGroup." + Constants.MOD_ID + ".blocks"))
                                .icon(() -> ModBlocks.SIGN_POST_ITEM.get().getDefaultInstance())
                                .displayItems((params, output) -> ModItems.populateBlocksTab(output))
                                .build());
                creativeTabs.register(Constants.MOD_ID, () -> CreativeModeTab.builder()
                                .title(Component.translatable("itemGroup." + Constants.MOD_ID + ".symbols"))
                                .icon(() -> ModItems.getSymbolStack(SymbolMaterial.IRON, SymbolType.LETTER_A))
                                .displayItems((params, output) -> ModItems.populateSymbolsTab(output))
                                .build());
                creativeTabs.register(Constants.MOD_ID + "_signs", () -> CreativeModeTab.builder()
                                .title(Component.translatable("itemGroup." + Constants.MOD_ID + ".signs"))
                                .icon(() -> ModItems.getSignStack(SignType.REGULATORY_GIVE_WAY))
                                .displayItems((params, output) -> ModItems.populateSignsTab(output))
                                .build());
        }

        @Override
        public void register() {
        }

        public void initialize(IEventBus eventBus) {
                creativeTabs.register(eventBus);
        }
}
