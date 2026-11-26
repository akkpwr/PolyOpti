package org.butbetter.polyopti.client.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.server.level.ParticleStatus;
import org.butbetter.polyopti.client.config.PolyOptiConfig;

public final class LowGraphics {

    private static boolean applied;
    private static ParticleStatus previousParticles = ParticleStatus.ALL;
    private static boolean previousEntityShadows = true;
    private static double previousScreenEffectScale = 1.0;

    private LowGraphics() {
    }

    public static void refresh() {
        PolyOptiConfig config = PolyOptiConfig.INSTANCE;
        boolean shouldApply = config.enabled && config.lowGraphicsMode;
        if (shouldApply == applied) {
            return;
        }

        Options options = Minecraft.getInstance().options;
        if (shouldApply) {
            previousParticles = options.particles().get();
            previousEntityShadows = options.entityShadows().get();
            previousScreenEffectScale = options.screenEffectScale().get();
            options.particles().set(ParticleStatus.MINIMAL);
            options.entityShadows().set(false);
            options.screenEffectScale().set(0.0);
            applied = true;
        } else {
            options.particles().set(previousParticles);
            options.entityShadows().set(previousEntityShadows);
            options.screenEffectScale().set(previousScreenEffectScale);
            applied = false;
        }
    }
}
