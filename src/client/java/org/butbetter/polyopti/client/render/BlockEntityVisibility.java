package org.butbetter.polyopti.client.render;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.phys.Vec3;
import org.butbetter.polyopti.client.config.PolyOptiConfig;

public final class BlockEntityVisibility {

    private BlockEntityVisibility() {
    }

    public static boolean shouldRender(BlockEntity blockEntity, Vec3 cameraPosition) {
        PolyOptiConfig config = PolyOptiConfig.INSTANCE;
        if (!config.enabled || !config.distanceLimitsEnabled || cameraPosition == null) {
            return true;
        }

        if (rendersBeyondDistance(blockEntity.getType())) {
            return true;
        }

        double limit = config.blockEntityDistance;
        if (limit <= 0.0) {
            return true;
        }

        BlockPos pos = blockEntity.getBlockPos();
        double dx = pos.getX() + 0.5 - cameraPosition.x();
        double dy = pos.getY() + 0.5 - cameraPosition.y();
        double dz = pos.getZ() + 0.5 - cameraPosition.z();
        return dx * dx + dy * dy + dz * dz <= limit * limit;
    }

    private static boolean rendersBeyondDistance(BlockEntityType<?> type) {
        return type == BlockEntityTypes.BEACON || type == BlockEntityTypes.CONDUIT || type == BlockEntityTypes.END_GATEWAY;
    }
}
