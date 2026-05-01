package com.drunkencod.factory_symbols.block;

import com.drunkencod.factory_symbols.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DisplayPanelBlockEntity extends BlockEntity {

    public static final String NBT_KEY_ITEM = "item";

    private ItemStack storedItem = ItemStack.EMPTY;

    public DisplayPanelBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.DISPLAY_PANEL_BE_TYPE.get(), pos, state);
    }

    // #region Data access

    public ItemStack getStoredItem() {
        return storedItem;
    }

    public void setStoredItem(ItemStack stack) {
        storedItem = stack.isEmpty() ? ItemStack.EMPTY : stack;
    }

    // #region NBT

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!storedItem.isEmpty())
            tag.put(NBT_KEY_ITEM, storedItem.save(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(NBT_KEY_ITEM))
            storedItem = ItemStack.parseOptional(registries, tag.getCompound(NBT_KEY_ITEM));
        else
            storedItem = ItemStack.EMPTY;
    }

    // #region Sync to client

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
