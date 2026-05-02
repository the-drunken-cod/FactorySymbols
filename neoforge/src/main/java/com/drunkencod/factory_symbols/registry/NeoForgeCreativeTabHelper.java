package com.drunkencod.factory_symbols.registry;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NeoForgeCreativeTabHelper implements ICreativeTabHelper {

    private final DeferredRegister<CreativeModeTab> creativeTabs = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
            Constants.MOD_ID);

    public NeoForgeCreativeTabHelper() {
        creativeTabs.register(Constants.MOD_ID, () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup." + Constants.MOD_ID))
                .icon(() -> ModItems.getSymbolStack(SymbolMaterial.IRON, SymbolType.LETTER_A))
                .displayItems((params, output) -> ModItems.populateCreativeTab(output))
                .build());
    }

    @Override
    public void register() {
    }

    public void initialize(IEventBus eventBus) {
        creativeTabs.register(eventBus);
    }
}
