package org.butbetter.polyopti.mixin.client;

import com.mojang.renderpearl.api.commands.RenderPass;
import net.minecraft.client.renderer.WorldBorderRenderer;
import net.minecraft.client.renderer.oit.OitStage;
import net.minecraft.client.renderer.state.level.WorldBorderRenderState;
import net.minecraft.world.phys.Vec3;
import org.butbetter.polyopti.client.config.PolyOptiConfig;
import org.butbetter.polyopti.client.util.Safeguard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldBorderRenderer.class)
public class WorldBorderRendererMixin {

    @Unique
    private static final String RENDER_DESCRIPTOR = "render(Lnet/minecraft/client/renderer/state/level/WorldBorderRenderState;Lcom/mojang/renderpearl/api/commands/RenderPass;Lnet/minecraft/world/phys/Vec3;D)V";
    @Unique
    private static final String RENDER_OIT_DESCRIPTOR = "renderOit(Lnet/minecraft/client/renderer/state/level/WorldBorderRenderState;Lnet/minecraft/world/phys/Vec3;DLnet/minecraft/client/renderer/oit/OitStage;Lcom/mojang/renderpearl/api/commands/RenderPass;)V";

    @Inject(method = RENDER_DESCRIPTOR, at = @At("HEAD"), cancellable = true, require = 0)
    private void polyopti$hideWorldBorder(WorldBorderRenderState state, RenderPass renderPass, Vec3 cameraPosition, double renderDistance, CallbackInfo callback) {
        if (hidden()) {
            callback.cancel();
        }
    }

    @Inject(method = RENDER_OIT_DESCRIPTOR, at = @At("HEAD"), cancellable = true, require = 0)
    private void polyopti$hideWorldBorderOit(WorldBorderRenderState state, Vec3 cameraPosition, double renderDistance, OitStage stage, RenderPass renderPass, CallbackInfo callback) {
        if (hidden()) {
            callback.cancel();
        }
    }

    @Unique
    private static boolean hidden() {
        return Safeguard.test("world_border", () -> {
            PolyOptiConfig config = PolyOptiConfig.INSTANCE;
            return config.enabled && config.hideWorldBorder;
        }, false);
    }
}
