package com.drunkencod.factory_symbols.mixin;

import com.drunkencod.factory_symbols.Constants;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(CallbackInfo info) {

        Constants.LOG.info("Factory Symbols common mixin loaded!");
        Constants.LOG.info("MC Version: {}", Minecraft.getInstance().getVersionType());
    }
}
