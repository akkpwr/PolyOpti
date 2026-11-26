package org.butbetter.polyopti.client.render;

import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import org.butbetter.polyopti.client.config.PolyOptiConfig;

public final class ParticlePolicy {

    private ParticlePolicy() {
    }

    public static boolean allowCreation(ParticleOptions options, double x, double y, double z) {
        PolyOptiConfig config = PolyOptiConfig.INSTANCE;
        if (!config.enabled) {
            return true;
        }

        if (config.hideLavaParticles && isLava(options)) {
            return false;
        }

        if (config.reduceParticles && ThreadLocalRandom.current().nextBoolean()) {
            return false;
        }

        double limit = config.particleDistance;
        if (limit <= 0.0) {
            return true;
        }

        Camera camera = Minecraft.getInstance().gameRenderer.mainCamera();
        if (camera == null || !camera.isInitialized()) {
            return true;
        }

        Vec3 position = camera.position();
        double dx = x - position.x();
        double dy = y - position.y();
        double dz = z - position.z();
        return dx * dx + dy * dy + dz * dz <= limit * limit;
    }

    public static boolean allowAdd(int activeParticles) {
        PolyOptiConfig config = PolyOptiConfig.INSTANCE;
        if (!config.enabled) {
            return true;
        }
        return activeParticles < (int) config.particleLimit;
    }

    private static boolean isLava(ParticleOptions options) {
        ParticleType<?> type = options.getType();
        return type == ParticleTypes.LAVA
                || type == ParticleTypes.DRIPPING_LAVA
                || type == ParticleTypes.FALLING_LAVA
                || type == ParticleTypes.LANDING_LAVA
                || type == ParticleTypes.DRIPPING_DRIPSTONE_LAVA
                || type == ParticleTypes.FALLING_DRIPSTONE_LAVA;
    }
}
