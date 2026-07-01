package com.drunkencod.symbols_n_signs.client;

import java.util.List;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

public final class MultiLineOverlay {

    private static List<Component> lines = List.of();
    private static long showTimeMs = -1;
    private static boolean animateColor = false;

    public static final long DISPLAY_MS = 3000L;
    public static final long FADE_MS = 1000L;
    private static final int LINE_HEIGHT = 10;

    private MultiLineOverlay() {}

    public static void show(List<Component> newLines) {
        show(newLines, false);
    }

    public static void show(List<Component> newLines, boolean animate) {
        if (newLines == null || newLines.isEmpty())
            return;
        lines = List.copyOf(newLines);
        showTimeMs = System.currentTimeMillis();
        animateColor = animate;
    }

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (lines.isEmpty() || showTimeMs < 0)
            return;

        long elapsed = System.currentTimeMillis() - showTimeMs;
        if (elapsed >= DISPLAY_MS) {
            showTimeMs = -1;
            return;
        }

        long remainingMs = DISPLAY_MS - elapsed;
        float alpha01 = remainingMs < FADE_MS ? (float) remainingMs / FADE_MS : 1.0f;
        int alpha = (int) (alpha01 * 255);
        if (alpha <= 8)
            return;

        Font font = Minecraft.getInstance().font;
        int lineCount = lines.size();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(guiGraphics.guiWidth() / 2f, guiGraphics.guiHeight() - 68, 0.0f);

        for (int i = 0; i < lineCount; i++) {
            Component line = lines.get(i);
            int color = animateColor
                    ? Mth.hsvToArgb((float) elapsed / 50_000.0f, 0.7f, 0.6f, alpha)
                    : FastColor.ARGB32.color(alpha, -1);
            int textWidth = font.width(line);
            int y = -4 - (lineCount - 1 - i) * LINE_HEIGHT;
            guiGraphics.drawStringWithBackdrop(font, line, -textWidth / 2, y, textWidth, color);
        }

        guiGraphics.pose().popPose();
    }
}
