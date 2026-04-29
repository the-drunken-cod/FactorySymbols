package com.drunkencod.factory_symbols.conditions;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.platform.Services;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;

public record FabricSymbolCondition(SymbolMaterial material, SymbolType symbol) implements ResourceCondition {

    private static final MapCodec<FabricSymbolCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.STRING.<SymbolMaterial>xmap(
                    s -> Arrays.stream(SymbolMaterial.values())
                            .filter(m -> m.getPrefix().equals(s))
                            .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown material: " + s)),
                    SymbolMaterial::getPrefix).fieldOf("material").forGetter(FabricSymbolCondition::material),
            Codec.STRING.<SymbolType>xmap(
                    s -> Arrays.stream(SymbolType.values())
                            .filter(t -> t.name().toLowerCase().equals(s))
                            .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown symbol: " + s)),
                    t -> t.name().toLowerCase()).fieldOf("symbol").forGetter(FabricSymbolCondition::symbol))
            .apply(inst, FabricSymbolCondition::new));

    public static final ResourceConditionType<FabricSymbolCondition> TYPE = ResourceConditionType.create(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "symbol_enabled"),
            MAP_CODEC);

    @Override
    public ResourceConditionType<?> getType() {
        return TYPE;
    }

    @Override
    public boolean test(HolderLookup.Provider registries) {
        return Services.CONFIG.isMaterialEnabled(material)
                && Services.CONFIG.isCategoryEnabled(symbol.getCategory())
                && Services.CONFIG.isSymbolEnabled(symbol);
    }
}
