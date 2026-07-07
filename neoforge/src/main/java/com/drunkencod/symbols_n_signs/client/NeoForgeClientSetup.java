package com.drunkencod.symbols_n_signs.client;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.client.renderer.DisplayPanelBlockEntityRenderer;
import com.drunkencod.symbols_n_signs.client.renderer.SignPostSignFixtureBlockEntityRenderer;
import com.drunkencod.symbols_n_signs.registry.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class NeoForgeClientSetup {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlocks.DISPLAY_PANEL_BE_TYPE.get(), DisplayPanelBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlocks.SIGN_POST_SIGN_FIXTURE_BE_TYPE.get(),
                SignPostSignFixtureBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "multi_line_overlay"),
                MultiLineOverlay::render);
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ExpandableTooltipHandler.append(event.getItemStack(), event.getToolTip());
    }
}
