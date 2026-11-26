package org.butbetter.polyopti.mixin.client;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.world.entity.Entity;
import org.butbetter.polyopti.client.render.EntityVisibility;
import org.butbetter.polyopti.client.util.Safeguard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelExtractor.class)
public class LevelExtractorMixin {

    @Inject(method = "isEntityVisible", at = @At("HEAD"), cancellable = true, require = 0)
    private void polyopti$cullEntity(Entity entity, Frustum frustum, double cameraX, double cameraY, double cameraZ, float partialTicks, long chunkFadeDuration, CallbackInfoReturnable<Boolean> callback) {
        boolean visible = Safeguard.test("entity_visibility", () -> EntityVisibility.shouldRender(entity, cameraX, cameraY, cameraZ), true);
        if (!visible) {
            callback.setReturnValue(false);
        }
    }
}
