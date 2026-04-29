package com.drunkencod.factory_symbols;

import com.drunkencod.factory_symbols.config.NeoForgeConfigHelper;
import com.drunkencod.factory_symbols.datagen.NeoForgeItemModelProvider;
import com.drunkencod.factory_symbols.platform.Services;
import com.drunkencod.factory_symbols.registry.NeoForgeRegistryHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(Constants.MOD_ID)
public class FactorySymbolsMod {

    public FactorySymbolsMod(IEventBus eventBus, ModContainer modContainer) {
        // Wire DeferredRegisters
        ((NeoForgeRegistryHelper) Services.REGISTRY).initialize(eventBus);

        // Register configs
        ((NeoForgeConfigHelper) Services.CONFIG).register(modContainer);

        eventBus.addListener(this::onGatherData);

        Constants.LOG.info("Hello from Factory Symbols (NeoForge)!");
        FactorySymbols.init();
    }

    private void onGatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(
                event.includeClient(),
                new NeoForgeItemModelProvider(event.getGenerator().getPackOutput(), event.getExistingFileHelper()));
    }
}
