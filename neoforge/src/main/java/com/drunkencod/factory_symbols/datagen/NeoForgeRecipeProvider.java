package com.drunkencod.factory_symbols.datagen;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.conditions.NeoForgeSymbolCondition;
import com.drunkencod.factory_symbols.registry.ModItems;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class NeoForgeRecipeProvider extends RecipeProvider {

    public NeoForgeRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        for (SymbolMaterial mat : SymbolMaterial.values()) {
            String prefix = mat.getPrefix();
            Item materialItem = mat.getMaterialItem();

            TagKey<Item> materialTag = TagKey.create(
                    Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "materials/" + prefix));

            // #region Stonecutter recipes: raw material or existing symbol → symbol
            // (conditioned)
            for (SymbolType sym : SymbolType.values()) {
                Item symbol = ModItems.SYMBOLS.get(mat).get(sym).get();
                RecipeOutput conditional = output.withConditions(new NeoForgeSymbolCondition(mat, sym));

                SingleItemRecipeBuilder
                        .stonecutting(Ingredient.of(materialItem), RecipeCategory.MISC, symbol, mat.getYield())
                        .unlockedBy("has_material_" + prefix, has(materialItem))
                        .save(conditional, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID,
                                "stonecutter/symbol_" + prefix + "_" + sym.getId()));

                SingleItemRecipeBuilder
                        .stonecutting(Ingredient.of(materialTag), RecipeCategory.MISC, symbol, 1)
                        .unlockedBy("has_symbol_" + prefix, has(materialTag))
                        .save(conditional, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID,
                                "stonecutter/symbol_" + prefix + "_" + sym.getId() + "_convert"));
            }
        }
    }
}
