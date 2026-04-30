package com.drunkencod.factory_symbols;

import com.drunkencod.factory_symbols.conditions.NeoForgeSymbolCondition;
import com.drunkencod.factory_symbols.config.NeoForgeConfigHelper;
import com.drunkencod.factory_symbols.datagen.NeoForgeItemModelProvider;
import com.drunkencod.factory_symbols.datagen.NeoForgeItemTagsProvider;
import com.drunkencod.factory_symbols.datagen.NeoForgeLanguageProvider;
import com.drunkencod.factory_symbols.datagen.NeoForgeRecipeProvider;
import com.drunkencod.factory_symbols.platform.Services;
import com.drunkencod.factory_symbols.registry.NeoForgeCreativeTabHelper;
import com.drunkencod.factory_symbols.registry.NeoForgeRegistryHelper;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@Mod(Constants.MOD_ID)
public class FactorySymbolsMod {

        public FactorySymbolsMod(IEventBus eventBus, ModContainer modContainer) {
                // Wire DeferredRegisters
                ((NeoForgeRegistryHelper) Services.REGISTRY).initialize(eventBus);
                ((NeoForgeCreativeTabHelper) Services.CREATIVE_TAB).initialize(eventBus);

                // Register configs
                ((NeoForgeConfigHelper) Services.CONFIG).register(modContainer);

                // #region Register condition codecs so the recipe condition can be deserialized
                DeferredRegister<MapCodec<? extends ICondition>> conditionCodecs = DeferredRegister
                                .create(NeoForgeRegistries.Keys.CONDITION_CODECS, Constants.MOD_ID);
                conditionCodecs.register("symbol_enabled", () -> NeoForgeSymbolCondition.CODEC);
                conditionCodecs.register(eventBus);

                eventBus.addListener(this::onGatherData);

                Constants.LOG.info("Hello from Factory Symbols (NeoForge)!");
                FactorySymbols.init();
        }

        private void onGatherData(GatherDataEvent event) {
                var generator = event.getGenerator();
                var output = generator.getPackOutput();

                generator.addProvider(event.includeClient(),
                                new NeoForgeItemModelProvider(output));

                generator.addProvider(event.includeClient(),
                                new NeoForgeLanguageProvider(output));

                generator.addProvider(event.includeServer(),
                                new NeoForgeRecipeProvider(output, event.getLookupProvider()));

                generator.addProvider(event.includeServer(),
                                new NeoForgeItemTagsProvider(output));
        }
}
