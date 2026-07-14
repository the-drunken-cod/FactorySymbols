package com.drunkencod.symbols_n_signs.registry;

import com.drunkencod.symbols_n_signs.Constants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Registry;

import java.util.function.Supplier;

public class FabricRegistryHelper implements IRegistryHelper {

    @Override
    public <T extends Item> Supplier<T> registerItem(String id, Supplier<T> factory) {
        T item = Registry.register(BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id), factory.get());
        return () -> item;
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> factory) {
        T block = Registry.register(BuiltInRegistries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id), factory.get());
        return () -> block;
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntityType(String id,
            Supplier<BlockEntityType<T>> factory) {
        BlockEntityType<T> type = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id), factory.get());
        return () -> type;
    }

    @Override
    public Supplier<SoundEvent> registerSoundEvent(String id, Supplier<SoundEvent> factory) {
        SoundEvent soundEvent = Registry.register(BuiltInRegistries.SOUND_EVENT,
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id), factory.get());
        return () -> soundEvent;
    }

    @Override
    public <T extends RecipeSerializer<?>> Supplier<T> registerRecipeSerializer(String id, Supplier<T> factory) {
        T serializer = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER,
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id), factory.get());
        return () -> serializer;
    }
}
