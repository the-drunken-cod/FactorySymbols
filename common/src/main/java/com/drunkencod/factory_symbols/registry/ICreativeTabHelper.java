package com.drunkencod.factory_symbols.registry;

public interface ICreativeTabHelper {

    /**
     * Registers the mod's creative tab with the platform's registry system.
     * On Fabric this performs the registration eagerly; on NeoForge it is a no-op
     * because registration is driven by the event bus (see
     * {@link com.drunkencod.factory_symbols.registry.NeoForgeCreativeTabHelper#initialize}).
     */
    void register();
}
