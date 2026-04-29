package com.drunkencod.factory_symbols;

import com.drunkencod.factory_symbols.config.FabricConfigHelper;
import com.drunkencod.factory_symbols.platform.Services;
import net.fabricmc.api.ModInitializer;

public class FactorySymbolsMod implements ModInitializer {

    @Override
    public void onInitialize() {
        // Register Cloth Config configs
        ((FabricConfigHelper) Services.CONFIG).register();

        Constants.LOG.info("Hello from Factory Symbols (Fabric)!");
        FactorySymbols.init();
    }
}
