package com.drunkencod.symbols_n_signs.item;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.registry.ModSoundEvents;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.sounds.SoundSource;

public class RatchetWrenchItem extends Item {
    public static final String ID = "ratchet_wrench";

    public RatchetWrenchItem(Properties properties) {
        super(properties.component(DataComponents.LORE, new ItemLore(List.of(
                (Component.empty()
                        .append(Component.translatable("item.symbols_n_signs.ratchet_wrench.tooltip.1"))
                        .withStyle(ChatFormatting.GRAY)),
                (Component.empty()
                        .append(Component.translatable("item.symbols_n_signs.ratchet_wrench.tooltip.2"))
                        .withStyle(ChatFormatting.GRAY))))));
    }

    // #region Click dispatch

    public static void handleWrenchLeftClick(Level level, BlockPos pos, BlockState state, Direction clickedFace,
            Player player) {
        if (!(state.getBlock() instanceof IWrenchConfigurable configurable))
            return;
        InteractionResult result = configurable.onWrenchLeftClick(level, pos, state, clickedFace, player);
        if (result.consumesAction())
            playChangeModeSound(level, pos, player);
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return false;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        Block block = level.getBlockState(pos).getBlock();

        if (!(block instanceof IWrenchConfigurable configurable))
            return InteractionResult.PASS;

        if (ctx.getPlayer() != null) {
            try {
                BlockState state = level.getBlockState(pos);
                InteractionResult result = configurable.onWrenchRightClick(level, pos, state, ctx.getClickedFace(),
                        ctx.getPlayer());
                if (result.consumesAction())
                    playUseSound(level, pos, ctx.getPlayer());
            } catch (Exception e) {
                Constants.LOG.error("Error handling wrench right-click on block " + block, e);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    // #region Sound

    private static void playChangeModeSound(Level level, BlockPos pos, Player player) {
        level.playSound(null, pos, ModSoundEvents.RATCHET_WRENCH_CHANGE_MODE.get(), SoundSource.PLAYERS, 1.0f, 2.0f);
    }

    private static void playUseSound(Level level, BlockPos pos, Player player) {
        level.playSound(player, pos, ModSoundEvents.RATCHET_WRENCH_USE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
    }

    // #region Mode storage (per-block in CUSTOM_DATA)

    public static int getSelectedMode(ItemStack wrench, ResourceLocation blockId) {
        return getSelectedModeByKey(wrench, blockId.toString());
    }

    public static void setSelectedMode(ItemStack wrench, ResourceLocation blockId, int mode) {
        setSelectedModeByKey(wrench, blockId.toString(), mode);
    }

    /**
     * Per-face variant for multi-face fixtures (e.g. Sign Fixture), where each
     * occupied face tracks its own selected mode independently. Single-face
     * fixtures should keep using the blockId-only overloads above instead.
     */
    public static int getSelectedMode(ItemStack wrench, ResourceLocation blockId, Direction face) {
        return getSelectedModeByKey(wrench, perFaceKey(blockId, face));
    }

    public static void setSelectedMode(ItemStack wrench, ResourceLocation blockId, Direction face, int mode) {
        setSelectedModeByKey(wrench, perFaceKey(blockId, face), mode);
    }

    private static String perFaceKey(ResourceLocation blockId, Direction face) {
        return blockId.toString() + ":" + face.getSerializedName();
    }

    private static int getSelectedModeByKey(ItemStack wrench, String key) {
        CompoundTag tag = wrench.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.contains(key) ? tag.getInt(key) : 0;
    }

    private static void setSelectedModeByKey(ItemStack wrench, String key, int mode) {
        CompoundTag tag = wrench.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        tag.putInt(key, mode);
        wrench.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static ItemStack getWrenchInHand(Player player) {
        ItemStack main = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (main.getItem() instanceof RatchetWrenchItem)
            return main;
        ItemStack off = player.getItemInHand(InteractionHand.OFF_HAND);
        if (off.getItem() instanceof RatchetWrenchItem)
            return off;
        return ItemStack.EMPTY;
    }
}
