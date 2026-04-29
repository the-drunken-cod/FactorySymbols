package com.drunkencod.factory_symbols.conditions;

import com.drunkencod.factory_symbols.platform.Services;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.Arrays;

public record NeoForgeSymbolCondition(SymbolMaterial material, SymbolType symbol) implements ICondition {

    public static final MapCodec<NeoForgeSymbolCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.STRING.<SymbolMaterial>xmap(
                    s -> Arrays.stream(SymbolMaterial.values())
                            .filter(m -> m.getPrefix().equals(s))
                            .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown material: " + s)),
                    SymbolMaterial::getPrefix).fieldOf("material").forGetter(NeoForgeSymbolCondition::material),
            Codec.STRING.<SymbolType>xmap(
                    s -> Arrays.stream(SymbolType.values())
                            .filter(t -> t.name().toLowerCase().equals(s))
                            .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown symbol: " + s)),
                    t -> t.name().toLowerCase()).fieldOf("symbol").forGetter(NeoForgeSymbolCondition::symbol))
            .apply(inst, NeoForgeSymbolCondition::new));

    @Override
    public boolean test(ICondition.IContext context) {
        return Services.CONFIG.isMaterialEnabled(material)
                && Services.CONFIG.isCategoryEnabled(symbol.getCategory())
                && Services.CONFIG.isSymbolEnabled(symbol);
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
