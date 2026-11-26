package org.butbetter.polyopti.client;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.butbetter.polyopti.client.render.FrameCache;
import org.butbetter.polyopti.client.world.BlockHiding;
import org.butbetter.polyopti.client.world.LowGraphics;

public final class PolyOptiRuntime {

    private PolyOptiRuntime() {
    }

    public static void onInitialize() {
        BlockHiding.refresh();
    }

    public static void onLevelChanged() {
        FrameCache.reset();
        BlockHiding.refresh();
        LowGraphics.refresh();
    }

    public static void onFrameStart() {
        Minecraft minecraft = Minecraft.getInstance();
        Camera camera = minecraft.gameRenderer.mainCamera();
        Vec3 position = camera != null && camera.isInitialized() ? camera.position() : null;
        FrameCache.beginFrame(position);
        LowGraphics.refresh();
    }

    public static void onBlockHidingChanged() {
        BlockHiding.refresh();
        reloadChunks();
    }

    public static void onSettingsChanged() {
        BlockHiding.refresh();
        LowGraphics.refresh();
        reloadChunks();
    }

    private static void reloadChunks() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null && minecraft.levelExtractor != null) {
            minecraft.levelExtractor.allChanged();
        }
    }
}
