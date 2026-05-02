package com.drunkencod.factory_symbols.datagen;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

public class NeoForgeItemTagsProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;

    public NeoForgeItemTagsProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "tags/item");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Map<String, List<String>> tagValues = new LinkedHashMap<>();

        for (SymbolMaterial mat : SymbolMaterial.values()) {
            String symbolId = Constants.MOD_ID + ":" + mat.getPrefix() + "_symbol";
            tagValues.computeIfAbsent("symbols", k -> new ArrayList<>()).add(symbolId);
            tagValues.computeIfAbsent("symbols/" + mat.getPrefix(), k -> new ArrayList<>()).add(symbolId);
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
