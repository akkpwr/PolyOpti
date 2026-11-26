package org.butbetter.polyopti.client.frame;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import org.butbetter.polyopti.client.config.PolyOptiConfig;

public final class FrameLimiter {

    private static final int HIGHEST_SUPPORTED_LIMIT = 260;

    private FrameLimiter() {
    }

    public static int resolve(int vanillaLimit) {
        PolyOptiConfig config = PolyOptiConfig.INSTANCE;
        if (!config.enabled) {
            return vanillaLimit;
        }

        Minecraft minecraft = Minecraft.getInstance();
        Window window = minecraft.getWindow();
        if (config.backgroundFpsEnabled && (!window.isFocused() || window.isIconified())) {
            return clamp((int) config.backgroundFps);
        }

        if (config.menuFpsEnabled && hasOpenScreen(minecraft)) {
            return Math.min(vanillaLimit, clamp((int) config.menuFps));
        }

        return vanillaLimit;
    }

    private static boolean hasOpenScreen(Minecraft minecraft) {
        return minecraft.gui != null && (minecraft.gui.screen() != null || minecraft.gui.overlay() != null);
    }

    private static int clamp(int limit) {
        return Math.clamp(limit, 1, HIGHEST_SUPPORTED_LIMIT);
    }
}
