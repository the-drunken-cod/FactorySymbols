package com.drunkencod.factory_symbols.datagen;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.conditions.FabricSymbolCondition;
import com.drunkencod.factory_symbols.registry.ModItems;
import com.drunkencod.factory_symbols.symbols.SymbolMaterial;
import com.drunkencod.factory_symbols.symbols.SymbolType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class FabricSymbolRecipeProvider extends FabricRecipeProvider {

    public FabricSymbolRecipeProvider(FabricDataOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        for (SymbolMaterial mat : SymbolMaterial.values()) {
            String prefix = mat.getPrefix();
            Item template = ModItems.TEMPLATES.get(mat).get();

            // #region Stonecutter recipes: template → symbol (conditioned)
            for (SymbolType sym : SymbolType.values()) {
                Item symbol = ModItems.SYMBOLS.get(mat).get(sym).get();
                RecipeOutput conditional = withConditions(output, new FabricSymbolCondition(mat, sym));

                SingleItemRecipeBuilder.stonecutting(Ingredient.of(template), RecipeCategory.MISC, symbol)
                        .unlockedBy("has_template_" + prefix, has(template))
                        .save(conditional, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID,
                                "stonecutter/symbol_" + prefix + "_" + sym.getId()));
            }

            // #region Uncrafting recipe: any symbol of this material → template (static)
            TagKey<Item> materialTag = TagKey.create(
                    net.minecraft.core.registries.Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "material/" + prefix));

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, template)
                    .requires(materialTag)
                    .unlockedBy("has_symbol_" + prefix, has(materialTag))
                    .save(output, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID,
                            "uncrafting/template_" + prefix));
        }
    }
}
