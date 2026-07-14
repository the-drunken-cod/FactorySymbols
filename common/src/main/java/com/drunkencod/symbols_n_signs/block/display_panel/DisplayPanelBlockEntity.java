package com.drunkencod.symbols_n_signs.block.display_panel;

import com.drunkencod.symbols_n_signs.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DisplayPanelBlockEntity extends BlockEntity implements WorldlyContainer {

    public static final String NBT_KEY_ITEM = "item";
    public static final String NBT_KEY_ROTATION = "rotation";
    public static final String NBT_KEY_SCALE = "scale";
    public static final String NBT_KEY_BRIGHT = "bright";

    public static final int ROTATION_COUNT = 8;
    public static final float MIN_SCALE = 0.5f;
    public static final float MAX_SCALE = 1.0f;
    public static final float SCALE_STEP = 0.1f;

    private static final int[] SLOTS = new int[] { 0 };

    private ItemStack storedItem = ItemStack.EMPTY;
    private int rotation = 0;
    private float scale = 1.0f;
    private boolean bright = false;

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

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
        syncToClient();
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
        syncToClient();
    }

    public boolean isBright() {
        return bright;
    }

    public void setBright(boolean bright) {
        this.bright = bright;
        syncToClient();
    }

    private boolean isLocked() {
        return level != null && level.getBlockState(worldPosition).getValue(DisplayPanelBlock.LOCKED);
    }

    // #region NBT

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(NBT_KEY_ITEM, storedItem.saveOptional(registries));
        tag.putInt(NBT_KEY_ROTATION, rotation);
        tag.putFloat(NBT_KEY_SCALE, scale);
        tag.putBoolean(NBT_KEY_BRIGHT, bright);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(NBT_KEY_ITEM))
            storedItem = ItemStack.parseOptional(registries, tag.getCompound(NBT_KEY_ITEM));
        else
            storedItem = ItemStack.EMPTY;
        rotation = tag.getInt(NBT_KEY_ROTATION);
        scale = tag.contains(NBT_KEY_SCALE) ? tag.getFloat(NBT_KEY_SCALE) : 1.0f;
        bright = tag.getBoolean(NBT_KEY_BRIGHT);
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

    // #region WorldlyContainer

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return storedItem.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot == 0 ? storedItem : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot != 0)
            return ItemStack.EMPTY;
        ItemStack result = storedItem.split(amount);
        if (storedItem.isEmpty())
            storedItem = ItemStack.EMPTY;
        syncToClient();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot != 0)
            return ItemStack.EMPTY;
        ItemStack result = storedItem;
        storedItem = ItemStack.EMPTY;
        return result;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot != 0)
            return;
        storedItem = stack;
        syncToClient();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        storedItem = ItemStack.EMPTY;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return index == 0 && storedItem.isEmpty() && !isLocked();
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 0 && !isLocked();
    }

    private void syncToClient() {
        setChanged();
        if (level != null && !level.isClientSide())
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }
}
