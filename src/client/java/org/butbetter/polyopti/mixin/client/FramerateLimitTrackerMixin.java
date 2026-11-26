package org.butbetter.polyopti.mixin.client;

import com.mojang.blaze3d.platform.FramerateLimitTracker;
import org.butbetter.polyopti.client.frame.FrameLimiter;
import org.butbetter.polyopti.client.util.Safeguard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FramerateLimitTracker.class)
public class FramerateLimitTrackerMixin {

    @Shadow
    private int framerateLimit;

    @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true, require = 0)
    private void polyopti$applyDynamicLimit(CallbackInfoReturnable<Integer> callback) {
        Safeguard.run("frame_limit", () -> {
            int limit = FrameLimiter.resolve(this.framerateLimit);
            if (limit != this.framerateLimit) {
                callback.setReturnValue(limit);
            }
        });
    }
}
