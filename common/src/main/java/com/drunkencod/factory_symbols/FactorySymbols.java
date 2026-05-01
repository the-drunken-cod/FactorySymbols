package com.drunkencod.factory_symbols;

import com.drunkencod.factory_symbols.platform.Services;
import com.drunkencod.factory_symbols.registry.ModBlocks;
import com.drunkencod.factory_symbols.registry.ModItems;

public class FactorySymbols {

    public static void init() {
        ModBlocks.register();
        ModItems.register();
        Services.CREATIVE_TAB.register();

        Constants.LOG.info("Hello from Factory Symbols on {}! we are currently in a {} environment!",
                Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
    }
}
