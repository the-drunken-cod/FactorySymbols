package com.drunkencod.symbols_n_signs.symbols;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public enum SymbolMaterial {
    // #region Entries
    COAL("coal", 2, true),
    IRON("iron", "iron_ingot", 2, false),
    REDSTONE("redstone", 1, true),
    EMERALD("emerald", 2, false),
    LAPIS("lapis", "lapis_lazuli", 1, true),
    GOLD("gold", "gold_ingot", 2, false);

    private final String prefix;
    private final int yield;
    /**
     * True when the symbol texture should use white foreground (dark background).
     */
    private final boolean lightForeground;
    private final String materialItemId;

    SymbolMaterial(String prefix, String materialItemId, int yield, boolean lightForeground) {
        this.prefix = prefix;
        this.materialItemId = materialItemId;
        this.yield = yield;
        this.lightForeground = lightForeground;
    }

    SymbolMaterial(String prefix, int yield, boolean lightForeground) {
        this(prefix, prefix, yield, lightForeground);
    }

    public String getPrefix() {
        return prefix;
    }

    public String getMaterialItemId() {
        return materialItemId;
    }

    public Item getMaterialItem() {
        return BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(materialItemId));
    }

    public int getYield() {
        return yield;
    }

    public boolean isLightForeground() {
        return lightForeground;
    }
}
