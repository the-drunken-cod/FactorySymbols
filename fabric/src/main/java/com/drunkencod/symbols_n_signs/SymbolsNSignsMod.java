package com.drunkencod.symbols_n_signs;

import com.drunkencod.symbols_n_signs.block.sign_post.AbstractSignPostFixtureBlock;
import com.drunkencod.symbols_n_signs.block.sign_post.SignPostBlock;
import com.drunkencod.symbols_n_signs.block.sign_post.SignPostNetworkUtil;
import com.drunkencod.symbols_n_signs.config.FabricConfigHelper;
import com.drunkencod.symbols_n_signs.item.IWrenchConfigurable;
import com.drunkencod.symbols_n_signs.item.RatchetWrenchItem;
import com.drunkencod.symbols_n_signs.platform.Services;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.state.BlockState;

public class SymbolsNSignsMod implements ModInitializer {

    @Override
    public void onInitialize() {
        // Register Cloth Config configs
        ((FabricConfigHelper) Services.CONFIG).register();

        // #region Wrench sneak right-click harvest

        UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
            if (!player.isShiftKeyDown())
                return InteractionResult.PASS;
            net.minecraft.world.item.ItemStack stack = player.getItemInHand(hand);
            if (!stack.is(SignPostNetworkUtil.TOOLS_WRENCH))
                return InteractionResult.PASS;
            net.minecraft.core.BlockPos pos = hit.getBlockPos();
            net.minecraft.world.level.block.state.BlockState state = world.getBlockState(pos);
            net.minecraft.world.level.block.Block block = state.getBlock();
            if (!(block instanceof SignPostBlock) && !(block instanceof AbstractSignPostFixtureBlock))
                return InteractionResult.PASS;
            player.swing(hand);
            if (!world.isClientSide())
                SignPostNetworkUtil.harvestWithWrench(stack, state, world, pos, player);
            return InteractionResult.SUCCESS;
        });

        // #region Wrench survival left-click

        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if (world.isClientSide())
                return InteractionResult.PASS;
            if (!(player.getMainHandItem().getItem() instanceof RatchetWrenchItem))
                return InteractionResult.PASS;
            BlockState state = world.getBlockState(pos);
            if (!(state.getBlock() instanceof IWrenchConfigurable))
                return InteractionResult.PASS;
            RatchetWrenchItem.handleWrenchLeftClick(world, pos, state, player);
            return InteractionResult.SUCCESS;
        });

        Constants.LOG.info("Hello from Symbols'n'Signs (Fabric)!");
        SymbolsNSigns.init();
    }
}
