package com.drunkencod.symbols_n_signs.item;

import java.util.List;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.registry.ModSoundEvents;
import com.drunkencod.symbols_n_signs.util.TooltipUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ConfigurationClipboardItem extends Item implements IExpandableTooltip {

    public static final String ID = "configuration_clipboard";

    public ConfigurationClipboardItem(Properties properties) {
        super(properties);
    }

    @Override
    public List<Component> getExpandedTooltip(ItemStack stack) {
        int count = ConfigurationClipboardUtil.countStoredConfigurations(stack);
        return TooltipUtil.combine(
                List.of(Component.translatable(
                        "item." + Constants.MOD_ID + ".configuration_clipboard.tooltip.stored_count."
                                + (count == 1 ? "1" : "n"),
                        count).withStyle(ChatFormatting.GRAY)),
                TooltipUtil.tooltipLines("item." + Constants.MOD_ID + ".configuration_clipboard.tooltip.1"),
                TooltipUtil.tooltipLines("item." + Constants.MOD_ID + ".configuration_clipboard.tooltip.2"),
                TooltipUtil.tooltipLines("item." + Constants.MOD_ID + ".configuration_clipboard.tooltip.3"),
                TooltipUtil.tooltipLines("item." + Constants.MOD_ID + ".configuration_clipboard.tooltip.4"));
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        playSound(level, player.blockPosition(), player, ModSoundEvents.CONFIGURATION_CLIPBOARD_CLEAR.get());
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        BlockPos pos = ctx.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (player == null)
            return InteractionResult.PASS;

        if (!(block instanceof IWrenchConfigurable configurable)) {
            player.displayClientMessage(
                    Component.translatable("generic.message." + Constants.MOD_ID + ".targeted_block_not_configurable")
                            .withStyle(ChatFormatting.RED),
                    true);
            return InteractionResult.PASS;
        }

        ItemStack clipboard = ctx.getItemInHand();
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);

        try {
            if (player.isShiftKeyDown())
                copyConfiguration(level, pos, state, configurable, clipboard, blockId, ctx, player);
            else
                pasteConfiguration(level, pos, state, configurable, clipboard, blockId, ctx, player);
        } catch (Exception e) {
            Constants.LOG.error("Error handling Configuration Clipboard interaction on block " + block, e);
            // blocked sound
            playSound(level, pos, player, ModSoundEvents.CONFIGURATION_CLIPBOARD_ERROR.get());
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    private static void copyConfiguration(Level level, BlockPos pos, BlockState state, IWrenchConfigurable configurable,
            ItemStack clipboard, ResourceLocation blockId, UseOnContext ctx, Player player) {
        if (!level.isClientSide()) {
            CompoundTag data = configurable.copyConfiguration(level, pos, state, ctx.getClickedFace(),
                    ctx.getClickLocation(), player);
            ConfigurationClipboardUtil.storeConfiguration(clipboard, blockId,
                    configurable.getConfigFormatVersion(), data);
        }
        player.displayClientMessage(
                Component.translatable("item." + Constants.MOD_ID + ".configuration_clipboard.message.copied",
                        state.getBlock().getName()),
                true);
        // writing on paper sound
        playSound(level, pos, player, ModSoundEvents.CONFIGURATION_CLIPBOARD_COPY.get());
    }

    private static void pasteConfiguration(Level level, BlockPos pos, BlockState state,
            IWrenchConfigurable configurable, ItemStack clipboard,
            ResourceLocation blockId, UseOnContext ctx, Player player) {
        CompoundTag data = ConfigurationClipboardUtil.getConfiguration(clipboard, blockId,
                configurable.getConfigFormatVersion());
        boolean changed = data != null && configurable.pasteConfiguration(level, pos, state, ctx.getClickedFace(),
                ctx.getClickLocation(), data, player);

        if (!changed) {
            player.displayClientMessage(
                    Component.translatable("item." + Constants.MOD_ID + ".configuration_clipboard.message.paste_failed",
                            state.getBlock().getName()).withStyle(ChatFormatting.RED),
                    true);
            // blocked sound
            playSound(level, pos, player, ModSoundEvents.CONFIGURATION_CLIPBOARD_ERROR.get());
            return;
        }

        player.displayClientMessage(
                Component.translatable("item." + Constants.MOD_ID + ".configuration_clipboard.message.pasted",
                        state.getBlock().getName()),
                true);
        // turning page sound
        playSound(level, pos, player, ModSoundEvents.CONFIGURATION_CLIPBOARD_PASTE.get());
    }

    private static void playSound(Level level, BlockPos pos, Player player, SoundEvent sound) {
        playSound(level, pos, player, sound, 0.0f);
    }

    private static void playSound(Level level, BlockPos pos, Player player, SoundEvent sound, float pitchMod) {
        RandomSource rand = level.getRandom();
        level.playSound(player, pos, sound, SoundSource.PLAYERS, 1.0f, 0.85f + pitchMod + rand.nextFloat() * 0.3f);
    }
}
