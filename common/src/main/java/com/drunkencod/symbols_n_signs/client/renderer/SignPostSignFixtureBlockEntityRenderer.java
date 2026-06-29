package com.drunkencod.symbols_n_signs.client.renderer;

import com.drunkencod.symbols_n_signs.block.sign_post.SignFixtureFaceData;
import com.drunkencod.symbols_n_signs.block.sign_post.SignFixtureGeometry;
import com.drunkencod.symbols_n_signs.block.sign_post.SignPostSignFixtureBlockEntity;
import com.drunkencod.symbols_n_signs.item.SignItem;
import com.drunkencod.symbols_n_signs.signs.SignType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Renders each occupied face of a Sign Fixture as two panes (front + back),
 * sharing its Stance/Rotation/Scale transform math with the collision shape
 * via {@link SignFixtureGeometry}.
 */
public class SignPostSignFixtureBlockEntityRenderer implements BlockEntityRenderer<SignPostSignFixtureBlockEntity> {

    public SignPostSignFixtureBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(SignPostSignFixtureBlockEntity be, float partialTick, PoseStack poseStack,
            MultiBufferSource buffers, int packedLight, int packedOverlay) {
        for (Direction face : Direction.values()) {
            SignFixtureFaceData data = be.getFaceData(face);
            if (data == null || data.getItem().isEmpty())
                continue;
            renderFace(face, data, poseStack, buffers, packedLight, packedOverlay);
        }
    }

    private void renderFace(Direction face, SignFixtureFaceData data, PoseStack poseStack,
            MultiBufferSource buffers, int packedLight, int packedOverlay) {
        ItemStack stack = data.getItem();
        if (!(stack.getItem() instanceof SignItem signItem))
            return;
        SignType signType = signItem.getSignType();

        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getModelManager()
                .getAtlas(InventoryMenu.BLOCK_ATLAS)
                .getSprite(signType.getTextureLocation());
        VertexConsumer atlasConsumer = buffers.getBuffer(RenderType.entityCutout(InventoryMenu.BLOCK_ATLAS));
        int light = data.isBright() ? LightTexture.FULL_BRIGHT : packedLight;

        Matrix4f pivot = SignFixtureGeometry.buildPivotTransform(face, data);

        poseStack.pushPose();
        poseStack.last().pose().mul(pivot);

        // Forward pane: always the plain sign texture:
        renderPane(atlasConsumer, poseStack, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(),
                data.getScale(), 1, light, packedOverlay, 1f, 1f, 1f);

        int dblSidedMode = data.getDoubleSidedMode();
        if (dblSidedMode != SignFixtureFaceData.DOUBLE_SIDED_OFF) {
            // Back pane: same texture, untinted, same reading orientation as the
            // front (not mirrored). The vertex winding below is reversed from the
            // front pane's (needed for correct face culling), which by itself
            // already produces a left-right mirror; swapping u0/u1 cancels
            // that back out so double-sided reads the same from either side:
            if (dblSidedMode == SignFixtureFaceData.DOUBLE_SIDED_ON)
                renderPane(atlasConsumer, poseStack, sprite.getU1(), sprite.getU0(), sprite.getV0(), sprite.getV1(),
                        data.getScale(), -1, light, packedOverlay, 1f, 1f, 1f);
            else
                renderPane(atlasConsumer, poseStack, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(),
                        data.getScale(), -1, light, packedOverlay, 1f, 1f, 1f);
        } else {
            // Back pane: generic "back of sign" artwork, masked to the sign's silhouette:
            ResourceLocation maskTexture = SignMaskTextureCache.getOrCreate(signType.getTextureLocation());
            VertexConsumer maskConsumer = buffers.getBuffer(RenderType.entityCutout(maskTexture));
            renderPane(maskConsumer, poseStack, 0f, 1f, 0f, 1f,
                    data.getScale(), -1, light, packedOverlay, 1f, 1f, 1f);
        }

        poseStack.popPose();
    }

    private static void renderPane(VertexConsumer consumer, PoseStack poseStack, float u0, float u1, float v0,
            float v1, float scale, int signSide, int light, int overlay, float r, float g, float b) {
        poseStack.pushPose();
        poseStack.translate(0f, 0f, signSide * SignFixtureGeometry.PANE_HALF_GAP);
        poseStack.scale(scale * SignFixtureGeometry.BASE_PANE_SIZE, scale * SignFixtureGeometry.BASE_PANE_SIZE, 1f);

        Matrix4f mat = poseStack.last().pose();
        Vector3f normal = poseStack.last().normal().transform(new Vector3f(0f, 0f, signSide));

        if (signSide > 0) {
            vertex(consumer, mat, normal, -0.5f, 0.5f, u0, v0, r, g, b, light, overlay);
            vertex(consumer, mat, normal, -0.5f, -0.5f, u0, v1, r, g, b, light, overlay);
            vertex(consumer, mat, normal, 0.5f, -0.5f, u1, v1, r, g, b, light, overlay);
            vertex(consumer, mat, normal, 0.5f, 0.5f, u1, v0, r, g, b, light, overlay);
        } else {
            vertex(consumer, mat, normal, 0.5f, 0.5f, u0, v0, r, g, b, light, overlay);
            vertex(consumer, mat, normal, 0.5f, -0.5f, u0, v1, r, g, b, light, overlay);
            vertex(consumer, mat, normal, -0.5f, -0.5f, u1, v1, r, g, b, light, overlay);
            vertex(consumer, mat, normal, -0.5f, 0.5f, u1, v0, r, g, b, light, overlay);
        }

        poseStack.popPose();
    }

    private static void vertex(VertexConsumer consumer, Matrix4f mat, Vector3f normal, float x, float y,
            float u, float v, float r, float g, float b, int light, int overlay) {
        consumer.addVertex(mat, x, y, 0f)
                .setColor(r, g, b, 1f)
                .setUv(u, v)
                .setOverlay(overlay)
                .setLight(light)
                .setNormal(normal.x(), normal.y(), normal.z());
    }
}
