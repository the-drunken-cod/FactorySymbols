package com.drunkencod.factory_symbols;

import com.drunkencod.factory_symbols.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

public class FactorySymbols {

    @SuppressWarnings("null")
    public static void init() {

        Constants.LOG.info("Hello from Factory Symbols on {}! we are currently in a {} environment!",
                Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
        Constants.LOG.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND));

        if (Services.PLATFORM.isModLoaded("factory_symbols")) {
            Constants.LOG.info("Hello to factory_symbols");
        }
    }
}
