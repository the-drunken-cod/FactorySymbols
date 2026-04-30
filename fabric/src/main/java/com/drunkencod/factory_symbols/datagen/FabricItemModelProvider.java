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
            String material = mat.getPrefix();
            String colorSuffix = mat.isLightForeground() ? "_white" : "_black";
            String ns = Constants.MOD_ID;

            // #region Symbol item models — self-contained with background (layer0) and
            // foreground (layer1)
            for (SymbolType sym : SymbolType.values()) {
                String shape = sym.getCategory().getShape().getId();
                String catFolder = sym.getCategory().getId().replaceAll("s$", "");
                String symName = sym.getId().startsWith(catFolder + "_")
                        ? sym.getId().substring(catFolder.length() + 1)
                        : sym.getId();
                JsonObject json = new JsonObject();
                json.addProperty("parent", "minecraft:item/generated");
                JsonObject textures = new JsonObject();
                textures.addProperty("layer0", ns + ":item/base/" + shape + "/" + material);
                textures.addProperty("layer1", ns + ":item/symbol/" + catFolder + "/" + symName + colorSuffix);
                json.add("textures", textures);
                futures.add(save(cache, "symbol_" + material + "_" + sym.getId(), json));
            }
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
