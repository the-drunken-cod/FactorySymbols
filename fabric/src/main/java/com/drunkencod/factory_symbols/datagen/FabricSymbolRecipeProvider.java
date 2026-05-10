package com.drunkencod.factory_symbols.datagen;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FabricSymbolRecipeProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;

    public FabricSymbolRecipeProvider(FabricDataOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "recipe");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (SymbolMaterial mat : SymbolMaterial.values()) {
            String prefix = mat.getPrefix();
            String materialItemId = "minecraft:" + mat.getMaterialItemId();
            String materialTagId = Constants.MOD_ID + ":symbols/" + prefix;

            // #region Stonecutter recipes: raw material or existing symbol → symbol
            // (conditioned)
            for (SymbolType sym : SymbolType.values()) {
                String resultId = Constants.MOD_ID + ":" + sym.getId() + "_" + prefix;

                // material → symbol
                JsonObject fromMaterial = buildStonecutterRecipe(
                        materialItemId, false, resultId, mat.getYield());
                futures.add(save(cache,
                        "stonecutter/symbol_" + prefix + "_" + sym.getId(),
                        fromMaterial));

                // any symbol of same material → this symbol
                JsonObject fromSymbol = buildStonecutterRecipe(
                        materialTagId, true, resultId, 1);
                futures.add(save(cache,
                        "stonecutter/symbol_" + prefix + "_" + sym.getId() + "_convert",
                        fromSymbol));
            }
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private static JsonObject buildStonecutterRecipe(
            String ingredientId, boolean isTag, String resultId, int count) {
        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:stonecutting");

        JsonObject ingredient = new JsonObject();
        if (isTag)
            ingredient.addProperty("tag", ingredientId);
        else
            ingredient.addProperty("item", ingredientId);
        recipe.add("ingredient", ingredient);

        JsonObject result = new JsonObject();
        result.addProperty("id", resultId);
        result.addProperty("count", count);
        recipe.add("result", result);

        return recipe;
    }

    private CompletableFuture<?> save(CachedOutput cache, String name, JsonObject json) {
        Path path = pathProvider.json(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
        return DataProvider.saveStable(cache, json, path);
    }

    @Override
    public String getName() {
        return "Factory Symbols Recipes";
    }
}
