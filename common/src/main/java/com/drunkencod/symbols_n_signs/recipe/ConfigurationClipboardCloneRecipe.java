package com.drunkencod.symbols_n_signs.recipe;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.item.ConfigurationClipboardItem;
import com.drunkencod.symbols_n_signs.registry.ModRecipeSerializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Same 3x3 layout as the base Configuration Clipboard recipe
 * ({@code configuration_clipboard.json}: wooden slab corners, sign post
 * material nugget mid-row sides, paper top/bottom-center), but with an
 * existing Configuration Clipboard in the very center slot instead of paper.
 * Yields 2 exact clones of that clipboard (including its stored
 * Configurations) rather than a blank one. There is intentionally no recipe
 * to merge two clipboards' stored Configurations together.
 */
public class ConfigurationClipboardCloneRecipe extends CustomRecipe {

    public static final String ID = "clone_configuration_clipboard";

    private static final TagKey<Item> WOODEN_SLABS = ItemTags.WOODEN_SLABS;
    private static final TagKey<Item> NUGGETS = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sign_post_materials/nugget"));

    public ConfigurationClipboardCloneRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.width() != 3 || input.height() != 3)
            return false;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                ItemStack stack = input.getItem(x, y);
                boolean isCorner = (x == 0 || x == 2) && (y == 0 || y == 2);
                boolean isCenter = x == 1 && y == 1;
                if (isCorner) {
                    if (!stack.is(WOODEN_SLABS))
                        return false;
                } else if (isCenter) {
                    if (!(stack.getItem() instanceof ConfigurationClipboardItem))
                        return false;
                } else if (y == 1) {
                    if (!stack.is(NUGGETS))
                        return false;
                } else {
                    if (!stack.is(Items.PAPER))
                        return false;
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack source = input.getItem(1, 1);
        if (!(source.getItem() instanceof ConfigurationClipboardItem))
            return ItemStack.EMPTY;
        return source.copyWithCount(2);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CONFIGURATION_CLIPBOARD_CLONE.get();
    }
}
