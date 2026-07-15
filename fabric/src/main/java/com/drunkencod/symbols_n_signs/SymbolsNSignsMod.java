package com.drunkencod.symbols_n_signs;

import com.drunkencod.symbols_n_signs.block.sign_post.SignPostBlock;
import com.drunkencod.symbols_n_signs.block.sign_post.SignPostNetworkUtil;
import com.drunkencod.symbols_n_signs.config.FabricConfigHelper;
import com.drunkencod.symbols_n_signs.item.IWrenchConfigurable;
import com.drunkencod.symbols_n_signs.item.RatchetWrenchItem;
import com.drunkencod.symbols_n_signs.platform.Services;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.state.BlockState;

public class SymbolsNSignsMod implements ModInitializer {

    // Tracks every event tick (even skipped ones) to distinguish hold from new click.
    private static final Map<UUID, Long> lastWrenchEventTick = new HashMap<>();
    // Tracks when a mode switch last fired, for hold-repeat throttling.
    private static final Map<UUID, Long> lastWrenchActionTick = new HashMap<>();
    private static final int WRENCH_HOLD_REPEAT = 5;

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
            if (!(block instanceof SignPostBlock) && !(block instanceof IWrenchConfigurable))
                return InteractionResult.PASS;
            player.swing(hand);
            if (!world.isClientSide())
                SignPostNetworkUtil.harvestWithWrench(stack, state, world, pos, hit.getLocation(), player);
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

            UUID playerId = player.getUUID();
            long currentTick = world.getGameTime();
            long lastEventTick = lastWrenchEventTick.getOrDefault(playerId, -2L);
            lastWrenchEventTick.put(playerId, currentTick);

            boolean isHolding = currentTick - lastEventTick <= 1;
            long lastActionTick = lastWrenchActionTick.getOrDefault(playerId, (long) -WRENCH_HOLD_REPEAT);
            if (isHolding && currentTick - lastActionTick < WRENCH_HOLD_REPEAT)
                return InteractionResult.SUCCESS;

            lastWrenchActionTick.put(playerId, currentTick);
            RatchetWrenchItem.handleWrenchLeftClick(world, pos, state, direction, player);
            return InteractionResult.SUCCESS;
        });

        SymbolsNSigns.init();
    }
}
