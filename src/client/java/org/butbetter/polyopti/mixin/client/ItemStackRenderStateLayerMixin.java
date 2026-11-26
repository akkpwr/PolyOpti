package org.butbetter.polyopti.mixin.client;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.butbetter.polyopti.client.config.PolyOptiConfig;
import org.butbetter.polyopti.client.util.Safeguard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStackRenderState.LayerRenderState.class)
public class ItemStackRenderStateLayerMixin {

    @Shadow
    private ItemStackRenderState.FoilType foilType;

    @Inject(method = "setFoilType", at = @At("HEAD"), cancellable = true, require = 0)
    private void polyopti$hideGlint(ItemStackRenderState.FoilType type, CallbackInfo callback) {
        if (Safeguard.test("enchantment_glint", () -> {
            PolyOptiConfig config = PolyOptiConfig.INSTANCE;
            return config.enabled && config.hideEnchantmentGlint;
        }, false)) {
            this.foilType = ItemStackRenderState.FoilType.NONE;
            callback.cancel();
        }
    }
}
