package com.drunkencod.factory_symbols.datagen;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FabricItemModelProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;

    public FabricItemModelProvider(FabricDataOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (SymbolMaterial mat : SymbolMaterial.values()) {
            String prefix = mat.getPrefix();
            String fgSuffix = mat.isLightForeground() ? "_white" : "_black";
            String ns = Constants.MOD_ID;

            // #region Base background model
            futures.add(save(cache, "base_" + prefix,
                    model("minecraft:item/generated", "layer0", ns + ":item/bg_" + prefix)));

            // #region Template item model
            futures.add(save(cache, "template_" + prefix,
                    model("minecraft:item/generated", "layer0", ns + ":item/template_" + prefix)));

            // #region Symbol item models — inherits base, adds foreground layer
            for (SymbolType sym : SymbolType.values()) {
                JsonObject json = new JsonObject();
                json.addProperty("parent", ns + ":item/base_" + prefix);
                JsonObject textures = new JsonObject();
                textures.addProperty("layer1", ns + ":item/symbols/" + sym.getId() + fgSuffix);
                json.add("textures", textures);
                futures.add(save(cache, "symbol_" + prefix + "_" + sym.getId(), json));
            }
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> save(CachedOutput cache, String name, JsonObject json) {
        Path path = pathProvider.json(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
        return DataProvider.saveStable(cache, json, path);
    }

    private static JsonObject model(String parent, String textureKey, String textureValue) {
        JsonObject json = new JsonObject();
        json.addProperty("parent", parent);
        JsonObject textures = new JsonObject();
        textures.addProperty(textureKey, textureValue);
        json.add("textures", textures);
        return json;
    }

    @Override
    public String getName() {
        return "Factory Symbols Item Models";
    }
}
