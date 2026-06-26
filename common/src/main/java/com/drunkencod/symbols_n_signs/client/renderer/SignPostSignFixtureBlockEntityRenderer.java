package com.drunkencod.symbols_n_signs.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.drunkencod.symbols_n_signs.block.sign_post.SignFixtureFaceData;
import com.drunkencod.symbols_n_signs.block.sign_post.SignPostSignFixtureBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class SignPostSignFixtureBlockEntityRenderer implements BlockEntityRenderer<SignPostSignFixtureBlockEntity> {

    public SignPostSignFixtureBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(SignPostSignFixtureBlockEntity be, float partialTick, PoseStack poseStack,
            MultiBufferSource buffers, int packedLight, int packedOverlay) {
        for (Direction face : Direction.values()) {
            SignFixtureFaceData faceData = be.getFaceData(face);
            if (faceData == null)
                continue;
            ItemStack displayItem = faceData.getItem();
            if (!displayItem.isEmpty())
                renderFace(face, displayItem, be, partialTick, poseStack, buffers, packedLight, packedOverlay);
        }
    }

    protected void renderFace(Direction face, ItemStack displayItem, SignPostSignFixtureBlockEntity be,
            float partialTick, PoseStack poseStack, MultiBufferSource buffers, int packedLight, int packedOverlay) {
        BlockState state = be.getBlockState();

        poseStack.pushPose();

        // TODO:

        poseStack.popPose();
    }
}
