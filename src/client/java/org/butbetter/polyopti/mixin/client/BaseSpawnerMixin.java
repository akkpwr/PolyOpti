package org.butbetter.polyopti.mixin.client;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import org.butbetter.polyopti.client.config.PolyOptiConfig;
import org.butbetter.polyopti.client.util.Safeguard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BaseSpawner.class)
public class BaseSpawnerMixin {

    @Unique
    private static final String ADD_PARTICLE_TARGET = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V";

    @Redirect(method = "clientTick", at = @At(value = "INVOKE", target = ADD_PARTICLE_TARGET), require = 0)
    private void polyopti$skipSpawnerParticles(Level level, ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        boolean hidden = Safeguard.test("spawner_particles", () -> {
            PolyOptiConfig config = PolyOptiConfig.INSTANCE;
            return config.enabled && config.hideSpawnerParticles;
        }, false);
        if (!hidden) {
            level.addParticle(options, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
