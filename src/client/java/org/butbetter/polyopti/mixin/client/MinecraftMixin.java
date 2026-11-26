package org.butbetter.polyopti.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.butbetter.polyopti.client.PolyOptiRuntime;
import org.butbetter.polyopti.client.util.Safeguard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "setLevel", at = @At("RETURN"), require = 0)
    private void polyopti$onLevelChanged(ClientLevel level, CallbackInfo callback) {
        Safeguard.run("level_change", PolyOptiRuntime::onLevelChanged);
    }

    @Inject(method = "renderFrame", at = @At("HEAD"), require = 0)
    private void polyopti$onFrameStart(boolean advanceGameTime, CallbackInfo callback) {
        Safeguard.run("frame_start", PolyOptiRuntime::onFrameStart);
    }
}
