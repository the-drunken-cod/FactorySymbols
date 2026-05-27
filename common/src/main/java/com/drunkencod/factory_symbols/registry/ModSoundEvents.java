package com.drunkencod.factory_symbols.registry;

import com.drunkencod.factory_symbols.Constants;
import com.drunkencod.factory_symbols.platform.Services;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSoundEvents {

    // #region Ratchet Wrench
    public static final Supplier<SoundEvent> RATCHET_WRENCH_CHANGE_MODE = registerSoundEvent(
            "item.ratchet_wrench.change_mode");
    public static final Supplier<SoundEvent> RATCHET_WRENCH_USE = registerSoundEvent("item.ratchet_wrench.use");

    // #region register

    private static Supplier<SoundEvent> registerSoundEvent(String soundID) {
        return Services.REGISTRY.registerSoundEvent(
                soundID,
                () -> SoundEvent.createVariableRangeEvent(
                        ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, soundID)));
    }

    public static void register() {
        // no-op: class loading triggers static registration
    }
}
