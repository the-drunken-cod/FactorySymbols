package com.drunkencod.factory_symbols;

import com.drunkencod.factory_symbols.config.NeoForgeConfigHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.drunkencod.factory_symbols.datagen.NeoForgeItemModelProvider;
import com.drunkencod.factory_symbols.datagen.NeoForgeItemTagsProvider;
import com.drunkencod.factory_symbols.datagen.NeoForgeLanguageProvider;
import com.drunkencod.factory_symbols.datagen.NeoForgeRecipeProvider;
import com.drunkencod.factory_symbols.item.IWrenchConfigurable;
import com.drunkencod.factory_symbols.item.RatchetWrenchItem;
import com.drunkencod.factory_symbols.platform.Services;
import com.drunkencod.factory_symbols.registry.NeoForgeCreativeTabHelper;
import com.drunkencod.factory_symbols.registry.NeoForgeRegistryHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@Mod(Constants.MOD_ID)
public class FactorySymbolsMod {

        public FactorySymbolsMod(IEventBus eventBus, ModContainer modContainer) {
                // Wire DeferredRegisters
                ((NeoForgeRegistryHelper) Services.REGISTRY).initialize(eventBus);
                ((NeoForgeCreativeTabHelper) Services.CREATIVE_TAB).initialize(eventBus);

                // Register configs
                ((NeoForgeConfigHelper) Services.CONFIG).register(modContainer);

                eventBus.addListener(this::onGatherData);
                NeoForge.EVENT_BUS.addListener(FactorySymbolsMod::onLeftClickBlock);

                Constants.LOG.info("Hello from Factory Symbols (NeoForge)!");
                FactorySymbols.init();
        }

        // #region Wrench survival left-click

        private static final Map<UUID, Long> lastWrenchClickTick = new HashMap<>();
        private static final int WRENCH_CLICK_COOLDOWN = 5;

        private static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
                if (event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START)
                        return;
                if (!(event.getEntity().getMainHandItem().getItem() instanceof RatchetWrenchItem))
                        return;
                BlockState state = event.getLevel().getBlockState(event.getPos());
                if (!(state.getBlock() instanceof IWrenchConfigurable))
                        return;

                // Always cancel to prevent block breaking on both sides, even during cooldown.
                event.setCanceled(true);

                // In singleplayer, NeoForge fires this event on both the client-side
                // MultiPlayerGameMode and the server-side ServerPlayerGameMode via the shared
                // NeoForge.EVENT_BUS (same JVM). Running mode-switch logic client-side would
                // consume the shared cooldown slot before the server event fires at the same
                // tick, causing the server's cooldown check to always fail.
                if (event.getLevel().isClientSide())
                        return;

                UUID playerId = event.getEntity().getUUID();
                long currentTick = event.getLevel().getGameTime();
                if (currentTick - lastWrenchClickTick.getOrDefault(playerId, 0L) < WRENCH_CLICK_COOLDOWN)
                        return;
                lastWrenchClickTick.put(playerId, currentTick);

                RatchetWrenchItem.handleWrenchLeftClick(event.getLevel(), event.getPos(), state, event.getEntity());
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
