package com.drunkencod.symbols_n_signs.block.sign_post;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.registry.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/** Holds the up-to-6 independent per-face sign configurations of a Sign Fixture. */
public class SignPostSignFixtureBlockEntity extends AbstractSignPostFixtureBlockEntity {

    private static final String NBT_SIGNS = "signs";

    private final Map<Direction, SignFixtureFaceData> faces = new EnumMap<>(Direction.class);

    public SignPostSignFixtureBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.SIGN_POST_SIGN_FIXTURE_BE_TYPE.get(), pos, state);
    }

    // #region Data access

    public boolean isOccupied(Direction face) {
        return faces.containsKey(face);
    }

    public @Nullable SignFixtureFaceData getFaceData(Direction face) {
        return faces.get(face);
    }

    public Set<Direction> getOccupiedFaces() {
        return faces.keySet();
    }

    public void setFaceData(Direction face, SignFixtureFaceData data) {
        faces.put(face, data);
        Constants.LOG.debug("Sign Fixture at {} set face {} to item {}", worldPosition, face, data.getItem());
        syncToClient();
    }

    /** Clears the given face's slot and returns the item that was stored there (or EMPTY). */
    public ItemStack removeFace(Direction face) {
        SignFixtureFaceData removed = faces.remove(face);
        ItemStack item = removed != null ? removed.getItem() : ItemStack.EMPTY;
        Constants.LOG.debug("Sign Fixture at {} cleared face {} (returned {})", worldPosition, face, item);
        syncToClient();
        return item;
    }

    // #region NBT

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        CompoundTag signsTag = new CompoundTag();
        for (Map.Entry<Direction, SignFixtureFaceData> entry : faces.entrySet())
            signsTag.put(entry.getKey().getSerializedName(), entry.getValue().save(registries));
        tag.put(NBT_SIGNS, signsTag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        faces.clear();
        if (!tag.contains(NBT_SIGNS))
            return;
        CompoundTag signsTag = tag.getCompound(NBT_SIGNS);
        for (Direction dir : Direction.values()) {
            String key = dir.getSerializedName();
            if (signsTag.contains(key))
                faces.put(dir, SignFixtureFaceData.load(signsTag.getCompound(key), registries));
        }
    }

    // #region Sync

    private void syncToClient() {
        setChanged();
        if (level != null && !level.isClientSide())
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }
}
