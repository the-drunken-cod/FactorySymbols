package com.drunkencod.symbols_n_signs.client.renderer;

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
 * Lazily builds and caches a white-silhouette ("mask") variant of a sign's
 * item texture: every opaque pixel's RGB is forced to white, alpha
 * untouched. Vanilla's textured RenderTypes multiply the sampled texel by
 * the vertex color, so tinting the original (full-color) sign texture would
 * blend with its existing colors instead of replacing them outright; tinting
 * this white mask instead produces a flat solid color wherever the sign is
 * opaque.
 */
public final class SignMaskTextureCache {

    private static final Map<ResourceLocation, ResourceLocation> CACHE = new HashMap<>();

    private SignMaskTextureCache() {
    }

    public static ResourceLocation getOrCreate(ResourceLocation signTexture) {
        return CACHE.computeIfAbsent(signTexture, SignMaskTextureCache::build);
    }

    private static ResourceLocation build(ResourceLocation signTexture) {
        ResourceLocation filePath = signTexture.withPath(p -> "textures/" + p + ".png");
        try {
            Resource resource = Minecraft.getInstance().getResourceManager().getResourceOrThrow(filePath);
            NativeImage mask;
            try (InputStream stream = resource.open(); NativeImage source = NativeImage.read(stream)) {
                mask = new NativeImage(NativeImage.Format.RGBA, source.getWidth(), source.getHeight(), false);
                for (int y = 0; y < source.getHeight(); y++) {
                    for (int x = 0; x < source.getWidth(); x++) {
                        int alpha = FastColor.ABGR32.alpha(source.getPixelRGBA(x, y));
                        mask.setPixelRGBA(x, y, FastColor.ABGR32.color(alpha, 255, 255, 255));
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
