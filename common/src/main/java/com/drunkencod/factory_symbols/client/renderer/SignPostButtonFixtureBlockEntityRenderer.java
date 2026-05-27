package com.drunkencod.factory_symbols.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.drunkencod.factory_symbols.block.sign_post.AbstractSignPostFixtureBlockEntity;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

// NOTE: Once SignPostButtonFixtureBlock.disabled and SignPostButtonFixtureBlockEntity.java.disabled
// are renamed to .java, change the type parameter to SignPostButtonFixtureBlockEntity and update
// the registration in NeoForgeClientSetup + FactorySymbolsClientMod accordingly.
// TODO: implement renderFixture() once block models are ready.
public class SignPostButtonFixtureBlockEntityRenderer
        extends AbstractSignPostFixtureBlockEntityRenderer<AbstractSignPostFixtureBlockEntity> {

    public SignPostButtonFixtureBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderFixture(AbstractSignPostFixtureBlockEntity be, float partialTick, PoseStack poseStack,
            MultiBufferSource buffers, int packedLight, int packedOverlay) {
        // Render the fixture-specific button geometry here.
        // Look up the button model via:
        // Minecraft.getInstance().getBlockRenderer().getBlockModelShaper()
        // .getBlockModel(be.getBlockState())
        // then render it as a standalone BakedModel.
    }
}
