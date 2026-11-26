package org.butbetter.polyopti.client.render;

import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.butbetter.polyopti.client.config.PolyOptiConfig;

public final class DetailPolicy {

    private DetailPolicy() {
    }

    public static void apply(EntityRenderState state) {
        PolyOptiConfig config = PolyOptiConfig.INSTANCE;
        if (!config.enabled || state == null) {
            return;
        }

        double distanceSq = state.distanceToCameraSq;
        double nametagLimit = config.nametagDistance;
        if (state.nameTag != null && nametagLimit > 0.0 && distanceSq > nametagLimit * nametagLimit) {
            state.nameTag = null;
        }

        if (state instanceof AvatarRenderState avatar) {
            applyAvatar(avatar, config, distanceSq);
        }

        if (state instanceof LivingEntityRenderState living) {
            applyLiving(living, config, distanceSq);
        }
    }

    private static void applyAvatar(AvatarRenderState state, PolyOptiConfig config, double distanceSq) {
        if (config.hideStuckArrows) {
            state.arrowCount = 0;
            state.stingerCount = 0;
        }

        double cosmeticLimit = config.cosmeticDistance;
        if (cosmeticLimit > 0.0 && distanceSq > cosmeticLimit * cosmeticLimit) {
            state.showCape = false;
            state.parrotOnLeftShoulder = null;
            state.parrotOnRightShoulder = null;
        }
    }

    private static void applyLiving(LivingEntityRenderState state, PolyOptiConfig config, double distanceSq) {
        if (!config.simplifyDistantAnimations) {
            return;
        }

        double simplifyLimit = config.simplifyDistance;
        if (distanceSq > simplifyLimit * simplifyLimit) {
            state.walkAnimationPos = 0.0F;
            state.walkAnimationSpeed = 0.0F;
            state.ageInTicks = 0.0F;
        }
    }
}
