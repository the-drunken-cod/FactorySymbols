package com.drunkencod.symbols_n_signs.platform;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.platform.services.IPlatformHelper;
import com.drunkencod.symbols_n_signs.registry.ICreativeTabHelper;
import com.drunkencod.symbols_n_signs.registry.IRegistryHelper;
import com.drunkencod.symbols_n_signs.config.IConfigHelper;

import java.util.ServiceLoader;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IRegistryHelper REGISTRY = load(IRegistryHelper.class);
    public static final IConfigHelper CONFIG = load(IConfigHelper.class);
    public static final ICreativeTabHelper CREATIVE_TAB = load(ICreativeTabHelper.class);

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
