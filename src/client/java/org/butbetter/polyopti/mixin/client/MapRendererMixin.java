package org.butbetter.polyopti.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.MapRenderState;
import org.butbetter.polyopti.client.config.PolyOptiConfig;
import org.butbetter.polyopti.client.util.Safeguard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapRenderer.class)
public class MapRendererMixin {

    @Unique
    private static final String RENDER_DESCRIPTOR = "render(Lnet/minecraft/client/renderer/state/MapRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ZI)V";

    @Inject(method = RENDER_DESCRIPTOR, at = @At("HEAD"), cancellable = true, require = 0)
    private void polyopti$hideFramedMap(MapRenderState mapRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, boolean framed, int lightCoords, CallbackInfo callback) {
        if (!framed) {
            return;
        }

        if (Safeguard.test("framed_map", () -> {
            PolyOptiConfig config = PolyOptiConfig.INSTANCE;
            return config.enabled && config.hideFramedMaps;
        }, false)) {
            callback.cancel();
        }
    }
}
