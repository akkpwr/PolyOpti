package org.butbetter.polyopti.mixin.client;

import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.butbetter.polyopti.client.bridge.AbstractArrowAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractArrow.class)
public interface AbstractArrowAccessor extends AbstractArrowAccess {

    @Override
    @Invoker("isInGround")
    boolean polyopti$isInGround();
}
