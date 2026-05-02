package com.drunkencod.factory_symbols.config;

/**
 * Cross-loader config service interface.
 * <p>
 * Add config entries here as interface methods, then implement them in
 * {@code NeoForgeConfigHelper} (using {@code ModConfigSpec}) and
 * {@code FabricConfigHelper} (using Cloth Config / AutoConfig).
 *
 * <p>
 * Config is loaded via
 * {@link com.drunkencod.factory_symbols.platform.Services#CONFIG}.
 */
public interface IConfigHelper {

    boolean getExampleStartupBool();

    boolean getExampleServerBool();

    boolean getExampleClientBool();
}
