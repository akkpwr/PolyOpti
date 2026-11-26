package org.butbetter.polyopti.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.state.BeaconRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.butbetter.polyopti.client.config.PolyOptiConfig;
import org.butbetter.polyopti.client.util.Safeguard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeaconRenderer.class)
public class BeaconRendererMixin {

    @Unique
    private static final String SUBMIT_DESCRIPTOR = "submit(Lnet/minecraft/client/renderer/blockentity/state/BeaconRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V";

    @Inject(method = SUBMIT_DESCRIPTOR, at = @At("HEAD"), cancellable = true, require = 0)
    private void polyopti$hideBeam(BeaconRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo callback) {
        if (Safeguard.test("beacon_beam", () -> {
            PolyOptiConfig config = PolyOptiConfig.INSTANCE;
            return config.enabled && config.hideBeaconBeams;
        }, false)) {
            callback.cancel();
        }
    }
}
