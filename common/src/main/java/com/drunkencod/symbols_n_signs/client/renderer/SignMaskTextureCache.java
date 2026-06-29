package com.drunkencod.symbols_n_signs.client.renderer;

import com.drunkencod.symbols_n_signs.Constants;
import com.mojang.blaze3d.platform.NativeImage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.FastColor;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Lazily builds and caches a "back" variant of a sign's item texture: every
 * opaque pixel samples its RGB from {@code item/sign/back.png} at the same
 * pixel coordinates, alpha untouched. Used to render the backside of
 * single-sided signs with the generic back-of-sign artwork instead of the
 * sign's own face texture.
 */
public final class SignMaskTextureCache {

    private static final ResourceLocation BACK_TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID,
            "item/sign/back");

    private static final Map<ResourceLocation, ResourceLocation> CACHE = new HashMap<>();

    private SignMaskTextureCache() {
    }

    public static ResourceLocation getOrCreate(ResourceLocation signTexture) {
        return CACHE.computeIfAbsent(signTexture, SignMaskTextureCache::build);
    }

    private static ResourceLocation build(ResourceLocation signTexture) {
        ResourceLocation filePath = signTexture.withPath(p -> "textures/" + p + ".png");
        ResourceLocation backFilePath = BACK_TEXTURE.withPath(p -> "textures/" + p + ".png");
        try {
            Resource resource = Minecraft.getInstance().getResourceManager().getResourceOrThrow(filePath);
            Resource backResource = Minecraft.getInstance().getResourceManager().getResourceOrThrow(backFilePath);
            NativeImage mask;
            try (InputStream stream = resource.open();
                    InputStream backStream = backResource.open();
                    NativeImage source = NativeImage.read(stream);
                    NativeImage back = NativeImage.read(backStream)) {
                mask = new NativeImage(NativeImage.Format.RGBA, source.getWidth(), source.getHeight(), false);
                for (int y = 0; y < source.getHeight(); y++) {
                    for (int x = 0; x < source.getWidth(); x++) {
                        int alpha = FastColor.ABGR32.alpha(source.getPixelRGBA(x, y));
                        int backX = Math.min(x, back.getWidth() - 1);
                        int backY = Math.min(y, back.getHeight() - 1);
                        int backColor = back.getPixelRGBA(backX, backY);
                        // Keep back.png's packed RGB bits as-is and only splice in the
                        // source's alpha; recomposing channels via ABGR32.color(a, r, g, b)
                        // does not mirror the alpha/red/green/blue accessors and swaps R/B.
                        mask.setPixelRGBA(x, y, (backColor & 0x00FFFFFF) | (alpha << 24));
                    }
                }
            }
            ResourceLocation maskLocation = signTexture.withSuffix("_mask");
            Minecraft.getInstance().getTextureManager().register(maskLocation, new DynamicTexture(mask));
            return maskLocation;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to build sign mask texture for " + signTexture, e);
        }
    }
}
