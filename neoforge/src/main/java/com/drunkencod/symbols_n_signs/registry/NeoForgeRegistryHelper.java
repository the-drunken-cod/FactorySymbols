package com.drunkencod.symbols_n_signs.registry;

import com.drunkencod.symbols_n_signs.Constants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NeoForgeRegistryHelper implements IRegistryHelper {

    private final DeferredRegister<Item> items = DeferredRegister.create(BuiltInRegistries.ITEM, Constants.MOD_ID);
    private final DeferredRegister<Block> blocks = DeferredRegister.create(BuiltInRegistries.BLOCK, Constants.MOD_ID);
    private final DeferredRegister<BlockEntityType<?>> blockEntityTypes = DeferredRegister
            .create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private final DeferredRegister<SoundEvent> soundEvents = DeferredRegister
            .create(Registries.SOUND_EVENT, Constants.MOD_ID);
    private final DeferredRegister<RecipeSerializer<?>> recipeSerializers = DeferredRegister
            .create(Registries.RECIPE_SERIALIZER, Constants.MOD_ID);

    @Override
    public <T extends Item> Supplier<T> registerItem(String id, Supplier<T> factory) {
        return (Supplier<T>) items.register(id, factory);
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> factory) {
        return (Supplier<T>) blocks.register(id, factory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntityType(String id,
            Supplier<BlockEntityType<T>> factory) {
        return (Supplier<BlockEntityType<T>>) (Supplier<?>) blockEntityTypes.register(id, factory);
    }

    @Override
    public Supplier<SoundEvent> registerSoundEvent(String id, Supplier<SoundEvent> factory) {
        return soundEvents.register(id, factory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends RecipeSerializer<?>> Supplier<T> registerRecipeSerializer(String id, Supplier<T> factory) {
        return (Supplier<T>) (Supplier<?>) recipeSerializers.register(id, factory);
    }

    /**
     * Must be called in the NeoForge mod constructor with the mod event bus so that
     * DeferredRegisters can fire their registration events.
     */
    public void initialize(IEventBus eventBus) {
        items.register(eventBus);
        blocks.register(eventBus);
        blockEntityTypes.register(eventBus);
        soundEvents.register(eventBus);
        recipeSerializers.register(eventBus);
    }
}
