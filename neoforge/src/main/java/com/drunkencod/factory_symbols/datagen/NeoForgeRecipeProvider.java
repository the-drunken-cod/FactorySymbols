package com.drunkencod.factory_symbols.datagen;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.item.RetroreflectiveIronSheetItem;
import com.drunkencod.factory_symbols.signs.SignType;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NeoForgeRecipeProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;

    public NeoForgeRecipeProvider(PackOutput output) {
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

        // #region Stonecutter recipes: retroreflective iron sheet ↔ sign
        String signMaterialId = Constants.MOD_ID + ":" + RetroreflectiveIronSheetItem.ID;
        String signsTagId = Constants.MOD_ID + ":signs";

        for (SignType sign : SignType.values()) {
            String resultId = Constants.MOD_ID + ":sign_" + sign.getId();
            JsonObject fromMaterial = buildStonecutterRecipe(signMaterialId, false, resultId, 1);
            futures.add(save(cache, "stonecutter/sign_" + sign.getId(), fromMaterial));
        }

        JsonObject signToMaterial = buildStonecutterRecipe(signsTagId, true, signMaterialId, 1);
        futures.add(save(cache, "stonecutter/sign_to_retroreflective_iron_sheet", signToMaterial));

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
