package com.drunkencod.symbols_n_signs.block.sign_post;

import com.drunkencod.symbols_n_signs.signs.SignStance;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

/**
 * Immutable per-face configuration of a Sign Fixture slot: the contained
 * item plus its Stance, Rotation, Scale, double-sided and Bright settings.
 * Matches the NBT layout documented in
 * {@code docs/todo/sign_post_sign_fixture.md}.
 */
public final class SignFixtureFaceData {

    public static final float MIN_SCALE = 0.5f;
    public static final float MAX_SCALE = 2.5f;
    public static final float SCALE_STEP = 0.25f;

    public static final int ROTATION_COUNT = 8;

    public static final float MIN_OFFSET = -0.075f;
    public static final float MAX_OFFSET = 0.075f;
    public static final float OFFSET_STEP = 0.025f;

    private static final String NBT_ITEM = "item";
    private static final String NBT_STANCE = "stance";
    private static final String NBT_ROTATION = "rotation";
    private static final String NBT_SCALE = "scale";
    private static final String NBT_OFFSET = "offset";
    private static final String NBT_DOUBLE_SIDED = "double_sided";
    private static final String NBT_BRIGHT = "bright";

    private final ItemStack item;
    private final SignStance stance;
    private final int rotation;
    private final float scale;
    private final float offset;
    private final boolean doubleSided;
    private final boolean bright;

    public SignFixtureFaceData(ItemStack item, SignStance stance, int rotation, float scale, float offset,
            boolean doubleSided, boolean bright) {
        this.item = item;
        this.stance = stance;
        this.rotation = rotation;
        this.scale = scale;
        this.offset = offset;
        this.doubleSided = doubleSided;
        this.bright = bright;
    }

    /**
     * Default configuration for a newly-placed sign: flat, no rotation, 1x scale.
     */
    public static SignFixtureFaceData initial(ItemStack item) {
        return new SignFixtureFaceData(item, SignStance.FLAT, 0, 1.0f, 0f, false, false);
    }

    public ItemStack getItem() {
        return item;
    }

    public SignStance getStance() {
        return stance;
    }

    public int getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public float getOffset() {
        return offset;
    }

    public boolean isDoubleSided() {
        return doubleSided;
    }

    public boolean isBright() {
        return bright;
    }

    public SignFixtureFaceData withStance(SignStance newStance) {
        return new SignFixtureFaceData(item, newStance, rotation, scale, offset, doubleSided, bright);
    }

    public SignFixtureFaceData withRotation(int newRotation) {
        return new SignFixtureFaceData(item, stance, newRotation, scale, offset, doubleSided, bright);
    }

    public SignFixtureFaceData withScale(float newScale) {
        return new SignFixtureFaceData(item, stance, rotation, newScale, offset, doubleSided, bright);
    }

    public SignFixtureFaceData withOffset(float newOffset) {
        return new SignFixtureFaceData(item, stance, rotation, scale, newOffset, doubleSided, bright);
    }

    public SignFixtureFaceData withDoubleSided(boolean newDoubleSided) {
        return new SignFixtureFaceData(item, stance, rotation, scale, offset, newDoubleSided, bright);
    }

    public SignFixtureFaceData withBright(boolean newBright) {
        return new SignFixtureFaceData(item, stance, rotation, scale, offset, doubleSided, newBright);
    }

    // #region NBT

    public CompoundTag save(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.put(NBT_ITEM, item.saveOptional(registries));
        tag.putInt(NBT_STANCE, stance.ordinal());
        tag.putInt(NBT_ROTATION, rotation);
        tag.putFloat(NBT_SCALE, scale);
        tag.putFloat(NBT_OFFSET, offset);
        tag.putBoolean(NBT_DOUBLE_SIDED, doubleSided);
        tag.putBoolean(NBT_BRIGHT, bright);
        return tag;
    }

    public static SignFixtureFaceData load(CompoundTag tag, HolderLookup.Provider registries) {
        ItemStack item = tag.contains(NBT_ITEM)
                ? ItemStack.parseOptional(registries, tag.getCompound(NBT_ITEM))
                : ItemStack.EMPTY;
        SignStance[] values = SignStance.values();
        int stanceOrdinal = tag.getInt(NBT_STANCE);
        SignStance stance = stanceOrdinal >= 0 && stanceOrdinal < values.length ? values[stanceOrdinal]
                : SignStance.FLAT;
        int rotation = tag.getInt(NBT_ROTATION);
        float scale = tag.contains(NBT_SCALE) ? tag.getFloat(NBT_SCALE) : 1.0f;
        float offset = tag.contains(NBT_OFFSET) ? tag.getFloat(NBT_OFFSET) : 0f;
        boolean doubleSided = tag.getBoolean(NBT_DOUBLE_SIDED);
        boolean bright = tag.getBoolean(NBT_BRIGHT);
        return new SignFixtureFaceData(item, stance, rotation, scale, offset, doubleSided, bright);
    }
}
