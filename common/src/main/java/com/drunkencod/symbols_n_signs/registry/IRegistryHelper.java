package com.drunkencod.symbols_n_signs.registry;

import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * Cross-loader service interface for registering items and blocks.
 * <p>
 * Usage:
 * 
 * <pre>{@code
 * public static final Supplier<MyItem> MY_ITEM = Services.REGISTRY.registerItem("my_item", MyItem::new);
 * }</pre>
 * 
 * On NeoForge, call {@code Services.REGISTRY.initialize(eventBus)} in your mod
 * constructor
 * before any registrations are used.
 */
public interface IRegistryHelper {

    /**
     * Register an item under the mod's namespace.
     *
     * @param id      Registry path (e.g. {@code "my_item"})
     * @param factory Supplier that creates the item instance
     * @return A supplier that returns the registered item
     */
    <T extends Item> Supplier<T> registerItem(String id, Supplier<T> factory);

    /**
     * Register a block under the mod's namespace.
     *
     * @param id      Registry path (e.g. {@code "my_block"})
     * @param factory Supplier that creates the block instance
     * @return A supplier that returns the registered block
     */
    <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> factory);

    /**
     * Register a block entity type under the mod's namespace.
     *
     * @param id      Registry path (e.g. {@code "my_block_entity"})
     * @param factory Supplier that creates the {@link BlockEntityType} instance
     * @return A supplier that returns the registered block entity type
     */
    <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntityType(String id,
            Supplier<BlockEntityType<T>> factory);

    /**
     * Register a sound event under the mod's namespace.
     *
     * @param id      Registry path (e.g. {@code "my_sound"})
     * @param factory Supplier that creates the {@link SoundEvent} instance
     * @return A supplier that returns the registered sound event
     */
    Supplier<SoundEvent> registerSoundEvent(String id, Supplier<SoundEvent> factory);

    /**
     * Register a recipe serializer under the mod's namespace.
     *
     * @param id      Registry path (e.g. {@code "my_recipe"})
     * @param factory Supplier that creates the {@link RecipeSerializer} instance
     * @return A supplier that returns the registered recipe serializer
     */
    <T extends RecipeSerializer<?>> Supplier<T> registerRecipeSerializer(String id, Supplier<T> factory);
}
