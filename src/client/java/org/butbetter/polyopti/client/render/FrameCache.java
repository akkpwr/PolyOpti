package org.butbetter.polyopti.client.render;

import it.unimi.dsi.fastutil.ints.Int2ByteOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.world.phys.Vec3;

public final class FrameCache {

    public static final byte UNKNOWN = 0;
    public static final byte VISIBLE = 1;
    public static final byte OCCLUDED = 2;

    private static final int OCCLUSION_LIFETIME_FRAMES = 6;
    private static final int OCCLUSION_CHECKS_PER_FRAME = 64;
    private static final double CAMERA_RESET_DISTANCE_SQ = 16.0;

    private static final Int2ByteOpenHashMap OCCLUSION = new Int2ByteOpenHashMap();
    private static final Int2IntOpenHashMap OCCLUSION_FRAME = new Int2IntOpenHashMap();

    private static int frame;
    private static int occlusionChecks;
    private static boolean cameraKnown;
    private static double lastCameraX;
    private static double lastCameraY;
    private static double lastCameraZ;

    static {
        OCCLUSION.defaultReturnValue(UNKNOWN);
    }

    private FrameCache() {
    }

    public static void beginFrame() {
        frame++;
        occlusionChecks = 0;
    }

    public static void beginFrame(Vec3 cameraPosition) {
        beginFrame();
        if (cameraPosition == null) {
            return;
        }

        if (!cameraKnown) {
            cameraKnown = true;
            reset();
        } else {
            double dx = cameraPosition.x() - lastCameraX;
            double dy = cameraPosition.y() - lastCameraY;
            double dz = cameraPosition.z() - lastCameraZ;
            if (dx * dx + dy * dy + dz * dz > CAMERA_RESET_DISTANCE_SQ) {
                reset();
            }
        }

        lastCameraX = cameraPosition.x();
        lastCameraY = cameraPosition.y();
        lastCameraZ = cameraPosition.z();
    }

    public static byte occlusion(int entityId) {
        byte value = OCCLUSION.get(entityId);
        if (value == UNKNOWN) {
            return UNKNOWN;
        }
        return frame - OCCLUSION_FRAME.get(entityId) <= OCCLUSION_LIFETIME_FRAMES ? value : UNKNOWN;
    }

    public static boolean hasOcclusionBudget() {
        return occlusionChecks < OCCLUSION_CHECKS_PER_FRAME;
    }

    public static void putOcclusion(int entityId, boolean occluded) {
        occlusionChecks++;
        OCCLUSION.put(entityId, occluded ? OCCLUDED : VISIBLE);
        OCCLUSION_FRAME.put(entityId, frame);
    }

    public static void reset() {
        OCCLUSION.clear();
        OCCLUSION_FRAME.clear();
    }
}
