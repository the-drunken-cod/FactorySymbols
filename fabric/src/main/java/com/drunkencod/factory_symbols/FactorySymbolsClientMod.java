package com.drunkencod.factory_symbols;

import com.drunkencod.factory_symbols.client.renderer.DisplayPanelBlockEntityRenderer;
import com.drunkencod.factory_symbols.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class FactorySymbolsClientMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(ModBlocks.DISPLAY_PANEL_BE_TYPE.get(), DisplayPanelBlockEntityRenderer::new);
    }
}
