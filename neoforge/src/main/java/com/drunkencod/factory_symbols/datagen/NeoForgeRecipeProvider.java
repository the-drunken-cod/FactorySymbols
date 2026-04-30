package com.drunkencod.factory_symbols.datagen;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.conditions.NeoForgeSymbolCondition;
import com.drunkencod.factory_symbols.registry.ModItems;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
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
            Item template = ModItems.TEMPLATES.get(mat).get();

            // #region Stonecutter recipes: template → symbol (one per symbol, conditioned)
            for (SymbolType sym : SymbolType.values()) {
                Item symbol = ModItems.SYMBOLS.get(mat).get(sym).get();
                RecipeOutput conditional = output.withConditions(new NeoForgeSymbolCondition(mat, sym));

                SingleItemRecipeBuilder.stonecutting(Ingredient.of(template), RecipeCategory.MISC, symbol)
                        .unlockedBy("has_template_" + prefix, has(template))
                        .save(conditional, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID,
                                "stonecutter/symbol_" + prefix + "_" + sym.getId()));
            }

            // #region Crafting recipe: material item → template (static)
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, template, mat.getYield())
                    .requires(mat.getMaterialItem())
                    .unlockedBy("has_material_" + prefix, has(mat.getMaterialItem()))
                    .save(output, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID,
                            "crafting/template_" + prefix));

            // #region Uncrafting recipe: any symbol of this material → template (static)
            TagKey<Item> materialTag = TagKey.create(
                    net.minecraft.core.registries.Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "materials/" + prefix));

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, template)
                    .requires(materialTag)
                    .unlockedBy("has_symbol_" + prefix, has(materialTag))
                    .save(output, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID,
                            "uncrafting/template_" + prefix));
        }
    }
}
