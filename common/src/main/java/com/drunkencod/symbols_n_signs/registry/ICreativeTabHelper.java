package com.drunkencod.symbols_n_signs.registry;

import com.drunkencod.symbols_n_signs.Constants;

public interface ICreativeTabHelper {
    public static final String TAB_MATERIALS_AND_BLOCKS_KEY = Constants.MOD_ID + "_0_materials_and_blocks";
    public static final String TAB_SYMBOLS_KEY = Constants.MOD_ID + "_1_symbols";
    public static final String TAB_SIGNS_KEY = Constants.MOD_ID + "_2_signs";

    public static final String TAB_MATERIALS_AND_BLOCKS_TR_KEY = "itemGroup." + Constants.MOD_ID
            + ".materials_and_blocks";
    public static final String TAB_SYMBOLS_TR_KEY = "itemGroup." + Constants.MOD_ID + ".symbols";
    public static final String TAB_SIGNS_TR_KEY = "itemGroup." + Constants.MOD_ID + ".signs";

    /**
     * Registers the mod's creative tab with the platform's registry system.
     * On Fabric this performs the registration eagerly; on NeoForge it is a no-op
     * because registration is driven by the event bus (see
     * com.drunkencod.symbols_n_signs.registry.NeoForgeCreativeTabHelper#initialize)
     */
    void register();
}
