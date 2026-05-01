package com.drunkencod.factory_symbols.client;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.client.renderer.DisplayPanelBlockEntityRenderer;
import com.drunkencod.factory_symbols.registry.ModBlocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NeoForgeClientSetup {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlocks.DISPLAY_PANEL_BE_TYPE.get(), DisplayPanelBlockEntityRenderer::new);
    }
}
