package org.butbetter.polyopti.mixin.client;

import java.util.Map;
import java.util.Queue;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.ParticleOptions;
import org.butbetter.polyopti.client.bridge.ParticleCounter;
import org.butbetter.polyopti.client.render.ParticlePolicy;
import org.butbetter.polyopti.client.util.Safeguard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin implements ParticleCounter {

    @Shadow
    @Final
    private Map<ParticleRenderType, ParticleGroup<?>> particles;

    @Shadow
    @Final
    private Queue<Particle> particlesToAdd;

    @Inject(method = "createParticle", at = @At("HEAD"), cancellable = true, require = 0)
    private void polyopti$filterParticle(ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, CallbackInfoReturnable<Particle> callback) {
        boolean allowed = Safeguard.test("particle_filter", () -> ParticlePolicy.allowCreation(options, x, y, z), true);
        if (!allowed) {
            callback.setReturnValue(null);
        }
    }

    @Inject(method = "add", at = @At("HEAD"), cancellable = true, require = 0)
    private void polyopti$limitParticles(Particle particle, CallbackInfo callback) {
        boolean allowed = Safeguard.test("particle_limit", () -> ParticlePolicy.allowAdd(this.polyopti$activeParticles()), true);
        if (!allowed) {
            callback.cancel();
        }
    }

    @Override
    public int polyopti$activeParticles() {
        int total = this.particlesToAdd.size();
        for (ParticleGroup<?> group : this.particles.values()) {
            total += group.size();
        }
        return total;
    }
}
