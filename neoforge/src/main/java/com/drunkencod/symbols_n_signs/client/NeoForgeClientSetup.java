package com.drunkencod.symbols_n_signs.client;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.client.renderer.DisplayPanelBlockEntityRenderer;
// import com.drunkencod.symbols_n_signs.client.renderer.SignPostButtonFixtureBlockEntityRenderer;
import com.drunkencod.symbols_n_signs.registry.ModBlocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NeoForgeClientSetup {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlocks.DISPLAY_PANEL_BE_TYPE.get(), DisplayPanelBlockEntityRenderer::new);
        // event.registerBlockEntityRenderer(ModBlocks.SIGN_POST_BUTTON_FIXTURE_BE_TYPE.get(),
        // SignPostButtonFixtureBlockEntityRenderer::new);
    }
}
