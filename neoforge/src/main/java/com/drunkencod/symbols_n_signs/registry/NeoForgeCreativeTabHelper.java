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
        creativeTabs.register(ICreativeTabHelper.TAB_MATERIALS_AND_BLOCKS_KEY, () -> CreativeModeTab.builder()
                .title(Component.translatable(ICreativeTabHelper.TAB_MATERIALS_AND_BLOCKS_TR_KEY))
                .icon(() -> ModBlocks.SIGN_POST_ITEM.get().getDefaultInstance())
                .displayItems((params, output) -> ModItems.populateMaterialsAndBlocksTab(output))
                .build());
        creativeTabs.register(ICreativeTabHelper.TAB_SYMBOLS_KEY, () -> CreativeModeTab.builder()
                .title(Component.translatable(ICreativeTabHelper.TAB_SYMBOLS_TR_KEY))
                .icon(() -> ModItems.getSymbolStack(SymbolMaterial.IRON, SymbolType.LETTER_A))
                .displayItems((params, output) -> ModItems.populateSymbolsTab(output))
                .build());
        creativeTabs.register(ICreativeTabHelper.TAB_SIGNS_KEY, () -> CreativeModeTab.builder()
                .title(Component.translatable(ICreativeTabHelper.TAB_SIGNS_TR_KEY))
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
