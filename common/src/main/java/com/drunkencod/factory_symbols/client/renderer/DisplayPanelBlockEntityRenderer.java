package com.drunkencod.factory_symbols.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.block.display_panel.DisplayPanelBlock;
import com.drunkencod.factory_symbols.block.display_panel.DisplayPanelBlockEntity;
import com.drunkencod.factory_symbols.platform.Services;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class DisplayPanelBlockEntityRenderer implements BlockEntityRenderer<DisplayPanelBlockEntity> {

    public DisplayPanelBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(DisplayPanelBlockEntity be, float partialTick, PoseStack poseStack,
            MultiBufferSource buffers, int packedLight, int packedOverlay) {
        ItemStack symbol = be.getStoredItem();
        if (symbol.isEmpty())
            return;

        BlockState state = be.getBlockState();
        Direction facing = state.getValue(DisplayPanelBlock.FACING);

        poseStack.pushPose();

        // center position, then rotate
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(switch (facing) {
            case SOUTH -> 0f;
            case EAST -> 90f;
            case NORTH -> 180f;
            case WEST -> 270f;
            default -> 0f;
        }));

        boolean isFactorySymbol = symbol
                .is(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "symbols")));

        // move to display surface, re-center
        if (isFactorySymbol)
            poseStack.translate((0.5f / 16f), -(0.5f / 16f), -(1f / 16f) * 7f);
        else
            poseStack.translate(0f, 0f, -(1f / 16f) * 7f);

        // flip horizontally
        poseStack.mulPose(Axis.YP.rotationDegrees(180f));

        // scale down and shift a tiny bit against z-fighting:
        poseStack.scale(0.995f, 0.995f, 0.995f);
        if (Services.CONFIG.displayPanelShiftRenderedItem()) {
            float cardinalFactor = facing == Direction.NORTH || facing == Direction.EAST ? 1f : -1f;
            poseStack.translate(0.00025f, 0.00025f * cardinalFactor, 0.00025f);
        }

        // render item
        Minecraft.getInstance().getItemRenderer().renderStatic(
                symbol,
                ItemDisplayContext.FIXED,
                packedLight,
                packedOverlay,
                poseStack,
                buffers,
                be.getLevel(),
                0);

        poseStack.popPose();
    }
}
