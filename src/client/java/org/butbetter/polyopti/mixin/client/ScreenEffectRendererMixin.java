package org.butbetter.polyopti.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.butbetter.polyopti.client.config.PolyOptiConfig;
import org.butbetter.polyopti.client.util.Safeguard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @Inject(method = "submitFire", at = @At("HEAD"), cancellable = true, require = 0)
    private static void polyopti$hideScreenFire(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, TextureAtlasSprite sprite, CallbackInfo callback) {
        if (Safeguard.test("screen_fire", () -> {
            PolyOptiConfig config = PolyOptiConfig.INSTANCE;
            return config.enabled && config.hideFirstPersonFire;
        }, false)) {
            callback.cancel();
        }
    }
}
