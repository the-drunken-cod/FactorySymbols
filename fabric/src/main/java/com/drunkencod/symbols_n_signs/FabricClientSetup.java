package com.drunkencod.symbols_n_signs;

import com.drunkencod.symbols_n_signs.client.MultiLineOverlay;
import com.drunkencod.symbols_n_signs.client.renderer.DisplayPanelBlockEntityRenderer;
import com.drunkencod.symbols_n_signs.client.renderer.SignPostSignFixtureBlockEntityRenderer;
import com.drunkencod.symbols_n_signs.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class FabricClientSetup implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(ModBlocks.DISPLAY_PANEL_BE_TYPE.get(), DisplayPanelBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlocks.SIGN_POST_SIGN_FIXTURE_BE_TYPE.get(),
                SignPostSignFixtureBlockEntityRenderer::new);
        HudRenderCallback.EVENT.register(MultiLineOverlay::render);
    }
}
