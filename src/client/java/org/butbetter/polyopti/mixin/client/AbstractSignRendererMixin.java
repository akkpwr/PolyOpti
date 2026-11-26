package org.butbetter.polyopti.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import net.minecraft.client.renderer.blockentity.state.SignRenderState;
import net.minecraft.world.level.block.entity.SignText;
import org.butbetter.polyopti.client.config.PolyOptiConfig;
import org.butbetter.polyopti.client.util.Safeguard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSignRenderer.class)
public class AbstractSignRendererMixin {

    @Inject(method = "submitSignText", at = @At("HEAD"), cancellable = true, require = 0)
    private void polyopti$hideSignText(SignRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, SignText signText, CallbackInfo callback) {
        if (Safeguard.test("sign_text", () -> {
            PolyOptiConfig config = PolyOptiConfig.INSTANCE;
            return config.enabled && config.hideSignText;
        }, false)) {
            callback.cancel();
        }
    }
}
