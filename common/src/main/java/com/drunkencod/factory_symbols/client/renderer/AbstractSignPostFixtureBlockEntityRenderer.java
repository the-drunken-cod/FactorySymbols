package com.drunkencod.factory_symbols.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.drunkencod.factory_symbols.block.sign_post.AbstractSignPostFixtureBlock;
import com.drunkencod.factory_symbols.block.sign_post.AbstractSignPostFixtureBlockEntity;
import com.drunkencod.factory_symbols.block.sign_post.SignPostNetworkUtil;
import com.drunkencod.factory_symbols.registry.ModBlocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.Map;

/**
 * Base renderer for all sign post fixture block entities.
 *
 * Renders the sign post center and branch segments by re-using the sign post
 * block's baked models, then delegates to {@link #renderFixture} for the
 * fixture-specific geometry.
 *
 * The center + branch rendering mirrors what the sign post multipart block
 * state JSON would produce, but done here so fixture block states can remain
 * minimal and the logic lives in one place.
 */
public abstract class AbstractSignPostFixtureBlockEntityRenderer<T extends AbstractSignPostFixtureBlockEntity>
        implements BlockEntityRenderer<T> {

    protected final BlockEntityRendererProvider.Context context;

    protected AbstractSignPostFixtureBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public final void render(T be, float partialTick, PoseStack poseStack,
            MultiBufferSource buffers, int packedLight, int packedOverlay) {
        BlockState state = be.getBlockState();

        // #region Render post center + branches

        // Build a sign post block state mirroring this fixture's connection booleans,
        // then use BlockRenderDispatcher to render the multipart sign post models.
        // This avoids duplicating geometry and keeps the fixture visuals consistent
        // with the sign post.
        Map<Direction, BooleanProperty> dirProps = SignPostNetworkUtil.getDirectionProperties();
        BlockState signPostState = ModBlocks.SIGN_POST.get().defaultBlockState()
                .setValue(AbstractSignPostFixtureBlock.POWERED, state.getValue(AbstractSignPostFixtureBlock.POWERED))
                .setValue(AbstractSignPostFixtureBlock.WATERLOGGED,
                        state.getValue(AbstractSignPostFixtureBlock.WATERLOGGED));

        for (Map.Entry<Direction, BooleanProperty> entry : dirProps.entrySet()) {
            signPostState = signPostState.setValue(entry.getValue(),
                    state.getValue(entry.getValue()));
        }

        Minecraft.getInstance().getBlockRenderer()
                .renderSingleBlock(signPostState, poseStack, buffers, packedLight, packedOverlay);

        // #region Render fixture-specific geometry

        renderFixture(be, partialTick, poseStack, buffers, packedLight, packedOverlay);
    }

    /**
     * Renders the fixture-specific geometry (button, lamp, sign, etc.) on top of
     * the already-rendered post segments.
     *
     * The PoseStack is in block-local space (origin at the block's corner).
     */
    protected abstract void renderFixture(T be, float partialTick, PoseStack poseStack,
            MultiBufferSource buffers, int packedLight, int packedOverlay);

    @Override
    public boolean shouldRenderOffScreen(T be) {
        return false;
    }
}
