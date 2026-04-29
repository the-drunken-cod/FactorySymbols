package com.drunkencod.factory_symbols.config;

import com.drunkencod.factory_symbols.symbols.SymbolCategory;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;

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

    // #region Symbol enable/disable (common config, checked at recipe load time)

    /** Whether to load recipes for items of the given material. */
    boolean isMaterialEnabled(SymbolMaterial material);

    /** Whether to load recipes for symbols in the given category. */
    boolean isCategoryEnabled(SymbolCategory category);

    /**
     * Whether to load the stonecutter recipe for this specific symbol (overrides
     * category/material).
     */
    boolean isSymbolEnabled(SymbolType symbol);
}
