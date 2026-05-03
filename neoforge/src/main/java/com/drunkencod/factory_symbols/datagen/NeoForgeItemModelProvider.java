package com.drunkencod.factory_symbols.datagen;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NeoForgeItemModelProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;

    public NeoForgeItemModelProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        String ns = Constants.MOD_ID;
        SymbolType[] types = SymbolType.values();

        for (SymbolMaterial mat : SymbolMaterial.values()) {
            String material = mat.getPrefix();
            String colorSuffix = mat.isLightForeground() ? "_white" : "_black";

            // #region Sub-models — one per symbol, placed in a subfolder
            for (SymbolType sym : types) {
                String catFolder = sym.getCategory().getId().replaceAll("s$", "");
                String symName = sym.getId().startsWith(catFolder + "_")
                        ? sym.getId().substring(catFolder.length() + 1)
                        : sym.getId();
                JsonObject json = new JsonObject();
                json.addProperty("parent", "minecraft:item/generated");
                JsonObject textures = new JsonObject();
                textures.addProperty("layer0", ns + ":item/base/" + sym.getShape().getId() + "/" + material);
                textures.addProperty("layer1", ns + ":item/symbol/" + catFolder + "/" + symName + colorSuffix);
                json.add("textures", textures);
                futures.add(save(cache, material + "_symbol/" + sym.getId(), json));
            }

            // #region Main model — delegates to sub-models via custom_model_data overrides
            JsonObject main = new JsonObject();
            main.addProperty("parent", "minecraft:item/generated");
            JsonObject mainTextures = new JsonObject();
            mainTextures.addProperty("layer0", ns + ":item/base/square/" + material);
            main.add("textures", mainTextures);
            JsonArray overrides = new JsonArray();
            for (SymbolType sym : types) {
                JsonObject entry = new JsonObject();
                JsonObject predicate = new JsonObject();
                predicate.addProperty("custom_model_data", sym.ordinal());
                entry.add("predicate", predicate);
                entry.addProperty("model", ns + ":item/" + material + "_symbol/" + sym.getId());
                overrides.add(entry);
            }
            main.add("overrides", overrides);
            futures.add(save(cache, material + "_symbol", main));
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> save(CachedOutput cache, String name, JsonObject json) {
        Path path = pathProvider.json(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
        return DataProvider.saveStable(cache, json, path);
    }

    @Override
    public String getName() {
        return "Factory Symbols Item Models";
    }
}
