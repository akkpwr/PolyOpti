package org.butbetter.polyopti.mixin.client;

import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.world.level.block.state.BlockState;
import org.butbetter.polyopti.client.util.Safeguard;
import org.butbetter.polyopti.client.world.BlockHiding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockStateModelSet.class)
public class BlockStateModelSetMixin {

    @Inject(method = "get", at = @At("RETURN"), cancellable = true, require = 0)
    private void polyopti$hideBlockModel(BlockState state, CallbackInfoReturnable<BlockStateModel> callback) {
        BlockStateModel original = callback.getReturnValue();
        BlockStateModel replaced = Safeguard.supply("block_model_hiding", () -> BlockHiding.hiddenModel(state, original), original);
        if (replaced != original) {
            callback.setReturnValue(replaced);
        }
    }
}
