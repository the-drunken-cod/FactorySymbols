package com.drunkencod.factory_symbols.symbols;

public enum SymbolMaterial {
    // #region Entries
    IRON("iron", 8, false),
    COAL("coal", 8, true),
    REDSTONE("redstone", 8, true),
    GOLD("gold", 16, false),
    EMERALD("emerald", 16, false),
    DIAMOND("diamond", 16, false),
    COPPER("copper", 8, false),
    AMETHYST("amethyst", 8, false);

    private final String prefix;
    private final int yield;
    /**
     * True when the symbol texture should use white foreground (dark background).
     */
    private final boolean lightForeground;

    SymbolMaterial(String prefix, int yield, boolean lightForeground) {
        this.prefix = prefix;
        this.yield = yield;
        this.lightForeground = lightForeground;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getYield() {
        return yield;
    }

    public boolean isLightForeground() {
        return lightForeground;
    }
}
