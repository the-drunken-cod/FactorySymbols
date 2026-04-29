package com.drunkencod.factory_symbols;

import com.drunkencod.factory_symbols.conditions.FabricSymbolCondition;
import com.drunkencod.factory_symbols.config.FabricConfigHelper;
import com.drunkencod.factory_symbols.platform.Services;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;

public class FactorySymbolsMod implements ModInitializer {

    @Override
    public void onInitialize() {
        // Register Cloth Config configs
        ((FabricConfigHelper) Services.CONFIG).register();

        // Register recipe condition type (needed at runtime for recipe loading)
        ResourceConditions.register(FabricSymbolCondition.TYPE);

        Constants.LOG.info("Hello from Factory Symbols (Fabric)!");
        FactorySymbols.init();
    }
}
