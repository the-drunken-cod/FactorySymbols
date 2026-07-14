package com.drunkencod.symbols_n_signs.registry;

import com.drunkencod.symbols_n_signs.platform.Services;
import com.drunkencod.symbols_n_signs.recipe.ConfigurationClipboardCloneRecipe;

import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import java.util.function.Supplier;

public class ModRecipeSerializers {

    // #region Configuration Clipboard
    public static final Supplier<SimpleCraftingRecipeSerializer<ConfigurationClipboardCloneRecipe>> CONFIGURATION_CLIPBOARD_CLONE = Services.REGISTRY
            .registerRecipeSerializer(
                    ConfigurationClipboardCloneRecipe.ID,
                    () -> new SimpleCraftingRecipeSerializer<>(ConfigurationClipboardCloneRecipe::new));

    public static void register() {
        // no-op: class loading triggers static registration
    }
}
