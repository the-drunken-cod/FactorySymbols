package com.drunkencod.symbols_n_signs;

import com.drunkencod.symbols_n_signs.block.sign_post.AbstractSignPostFixtureBlock;
import com.drunkencod.symbols_n_signs.block.sign_post.SignPostBlock;
import com.drunkencod.symbols_n_signs.block.sign_post.SignPostNetworkUtil;
import com.drunkencod.symbols_n_signs.config.NeoForgeConfigHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.drunkencod.symbols_n_signs.datagen.NeoForgeItemModelProvider;
import com.drunkencod.symbols_n_signs.datagen.NeoForgeItemTagsProvider;
import com.drunkencod.symbols_n_signs.datagen.NeoForgeLanguageProvider;
import com.drunkencod.symbols_n_signs.datagen.NeoForgeRecipeProvider;
import com.drunkencod.symbols_n_signs.item.IWrenchConfigurable;
import com.drunkencod.symbols_n_signs.item.RatchetWrenchItem;
import com.drunkencod.symbols_n_signs.platform.Services;
import com.drunkencod.symbols_n_signs.registry.NeoForgeCreativeTabHelper;
import com.drunkencod.symbols_n_signs.registry.NeoForgeRegistryHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@Mod(Constants.MOD_ID)
public class SymbolsNSignsMod {

        public SymbolsNSignsMod(IEventBus eventBus, ModContainer modContainer) {
                // Wire DeferredRegisters
                ((NeoForgeRegistryHelper) Services.REGISTRY).initialize(eventBus);
                ((NeoForgeCreativeTabHelper) Services.CREATIVE_TAB).initialize(eventBus);

                // Register configs
                ((NeoForgeConfigHelper) Services.CONFIG).register(modContainer);

                eventBus.addListener(this::onGatherData);
                NeoForge.EVENT_BUS.addListener(SymbolsNSignsMod::onLeftClickBlock);
                NeoForge.EVENT_BUS.addListener(SymbolsNSignsMod::onRightClickBlock);

                SymbolsNSigns.init();
        }

        // #region Wrench sneak right-click harvest

        private static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
                if (!event.getEntity().isShiftKeyDown())
                        return;
                ItemStack stack = event.getEntity().getItemInHand(event.getHand());
                if (!stack.is(SignPostNetworkUtil.TOOLS_WRENCH))
                        return;
                BlockState state = event.getLevel().getBlockState(event.getPos());
                Block block = state.getBlock();
                if (!(block instanceof SignPostBlock) && !(block instanceof AbstractSignPostFixtureBlock))
                        return;

                event.setCanceled(true);
                event.getEntity().swing(event.getHand());

                if (event.getLevel().isClientSide())
                        return;

                SignPostNetworkUtil.harvestWithWrench(stack, state, event.getLevel(), event.getPos(),
                                event.getHitVec().getLocation(), event.getEntity());
        }

        // #region Wrench survival left-click

        // Tracks every event tick (even skipped ones) to distinguish hold from new click.
        private static final Map<UUID, Long> lastWrenchEventTick = new HashMap<>();
        // Tracks when a mode switch last fired, for hold-repeat throttling.
        private static final Map<UUID, Long> lastWrenchActionTick = new HashMap<>();
        private static final int WRENCH_HOLD_REPEAT = 5;

        private static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
                if (event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START)
                        return;
                if (!(event.getEntity().getMainHandItem().getItem() instanceof RatchetWrenchItem))
                        return;
                BlockState state = event.getLevel().getBlockState(event.getPos());
                if (!(state.getBlock() instanceof IWrenchConfigurable))
                        return;

                // Always cancel to prevent block breaking on both sides, even while held.
                event.setCanceled(true);

                // In singleplayer, NeoForge fires this event on both the client-side
                // MultiPlayerGameMode and the server-side ServerPlayerGameMode via the shared
                // NeoForge.EVENT_BUS (same JVM). Only run mode-switch logic server-side.
                if (event.getLevel().isClientSide())
                        return;

                UUID playerId = event.getEntity().getUUID();
                long currentTick = event.getLevel().getGameTime();
                long lastEventTick = lastWrenchEventTick.getOrDefault(playerId, -2L);
                lastWrenchEventTick.put(playerId, currentTick);

                boolean isHolding = currentTick - lastEventTick <= 1;
                long lastActionTick = lastWrenchActionTick.getOrDefault(playerId, (long) -WRENCH_HOLD_REPEAT);
                if (isHolding && currentTick - lastActionTick < WRENCH_HOLD_REPEAT)
                        return;

                lastWrenchActionTick.put(playerId, currentTick);
                RatchetWrenchItem.handleWrenchLeftClick(event.getLevel(), event.getPos(), state, event.getFace(),
                                event.getEntity());
        }

        private void onGatherData(GatherDataEvent event) {
                var generator = event.getGenerator();
                var output = generator.getPackOutput();

                generator.addProvider(event.includeClient(),
                                new NeoForgeItemModelProvider(output));

                generator.addProvider(event.includeClient(),
                                new NeoForgeLanguageProvider(output));

                generator.addProvider(event.includeServer(),
                                new NeoForgeRecipeProvider(output));

                generator.addProvider(event.includeServer(),
                                new NeoForgeItemTagsProvider(output));
        }
}
