package com.drunkencod.symbols_n_signs;

import com.drunkencod.symbols_n_signs.platform.Services;
import com.drunkencod.symbols_n_signs.registry.ModBlocks;
import com.drunkencod.symbols_n_signs.registry.ModItems;
import com.drunkencod.symbols_n_signs.registry.ModSoundEvents;

public class SymbolsNSigns {
    public static void init() {
        ModBlocks.register();
        ModItems.register();
        ModSoundEvents.register();
        Services.CREATIVE_TAB.register();

        Constants.LOG.info(
                "Symbols'n'Signs successfully registered on platform {}! we are currently in a {} environment.",
                Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
    }
}
