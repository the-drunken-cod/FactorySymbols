package com.drunkencod.symbols_n_signs.registry;

import com.drunkencod.symbols_n_signs.Constants;
import com.drunkencod.symbols_n_signs.platform.Services;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSoundEvents {

    // #region Sign Fixture
    public static final Supplier<SoundEvent> SIGN_POST_SIGN_FIXTURE_ADD_ITEM = registerSoundEvent(
            "block.sign_post_sign_fixture.add_item");
    public static final Supplier<SoundEvent> SIGN_POST_SIGN_FIXTURE_REMOVE_ITEM = registerSoundEvent(
            "block.sign_post_sign_fixture.remove_item");

    // #region Ratchet Wrench
    public static final Supplier<SoundEvent> RATCHET_WRENCH_CHANGE_MODE = registerSoundEvent(
            "item.ratchet_wrench.change_mode");
    public static final Supplier<SoundEvent> RATCHET_WRENCH_USE = registerSoundEvent(
            "item.ratchet_wrench.use");

    // #region Configuration Clipboard
    public static final Supplier<SoundEvent> CONFIGURATION_CLIPBOARD_COPY = registerSoundEvent(
            "item.configuration_clipboard.copy");
    public static final Supplier<SoundEvent> CONFIGURATION_CLIPBOARD_PASTE = registerSoundEvent(
            "item.configuration_clipboard.paste");
    public static final Supplier<SoundEvent> CONFIGURATION_CLIPBOARD_CLEAR = registerSoundEvent(
            "item.configuration_clipboard.clear");
    public static final Supplier<SoundEvent> CONFIGURATION_CLIPBOARD_ERROR = registerSoundEvent(
            "item.configuration_clipboard.error");

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
