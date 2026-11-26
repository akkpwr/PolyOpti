package org.butbetter.polyopti.mixin.client;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.butbetter.polyopti.client.render.SoundPolicy;
import org.butbetter.polyopti.client.util.Safeguard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {

    @Inject(method = "play", at = @At("HEAD"), cancellable = true, require = 0)
    private void polyopti$skipDistantSound(SoundInstance sound, CallbackInfoReturnable<SoundEngine.PlayResult> callback) {
        if (Safeguard.test("sound_distance", () -> SoundPolicy.shouldSkip(sound), false)) {
            callback.setReturnValue(SoundEngine.PlayResult.NOT_STARTED);
        }
    }
}
