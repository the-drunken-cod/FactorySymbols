package com.drunkencod.factory_symbols.datagen;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.registry.ModItems;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FabricItemTagsProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;

    public FabricItemTagsProvider(FabricDataOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "tags/item");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Map<String, List<String>> tagValues = new LinkedHashMap<>();

        for (SymbolMaterial mat : SymbolMaterial.values()) {
            String prefix = mat.getPrefix();

            for (SymbolType sym : SymbolType.values()) {
                String symbolId = BuiltInRegistries.ITEM.getKey(ModItems.SYMBOLS.get(mat).get(sym).get()).toString();

                tagValues.computeIfAbsent("symbols", k -> new ArrayList<>()).add(symbolId);
                tagValues.computeIfAbsent("materials/" + prefix, k -> new ArrayList<>()).add(symbolId);
                tagValues.computeIfAbsent("categories/" + sym.getCategory().getId(), k -> new ArrayList<>())
                        .add(symbolId);
            }
        }

        List<CompletableFuture<?>> futures = new ArrayList<>();
        tagValues.forEach((tagPath, items) -> {
            JsonArray values = new JsonArray();
            items.forEach(values::add);
            JsonObject json = new JsonObject();
            json.add("values", values);

            Path path = pathProvider.json(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, tagPath));
            futures.add(DataProvider.saveStable(cache, json, path));
        });

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Factory Symbols Item Tags";
    }
}
