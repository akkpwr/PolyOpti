package org.butbetter.polyopti.client.render;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.butbetter.polyopti.client.bridge.AbstractArrowAccess;
import org.butbetter.polyopti.client.config.PolyOptiConfig;
import org.joml.Vector3fc;

public final class EntityVisibility {

    private static final double MIN_OCCLUSION_DISTANCE = 20.0;
    private static final double AGGRESSIVE_OCCLUSION_DISTANCE = 12.0;
    private static final double BEHIND_CAMERA_MARGIN = 4.0;
    private static final double OCCLUSION_TOLERANCE_SQ = 4.0;
    private static final float LARGE_ENTITY_SIZE = 8.0F;

    private EntityVisibility() {
    }

    public static boolean shouldRender(Entity entity, double cameraX, double cameraY, double cameraZ) {
        PolyOptiConfig config = PolyOptiConfig.INSTANCE;
        if (!config.enabled) {
            return true;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (entity == minecraft.getCameraEntity() || entity == minecraft.player) {
            return true;
        }

        if (isHiddenBySettings(entity, config)) {
            return false;
        }

        if (isProtected(minecraft, entity)) {
            return true;
        }

        double distanceSq = distanceSq(entity, cameraX, cameraY, cameraZ);
        if (config.distanceLimitsEnabled && distanceSq > maxDistanceSq(entity, config)) {
            return false;
        }

        CullMode mode = CullMode.byIndex(config.cullMode);
        if (!mode.atLeast(CullMode.BALANCED)) {
            return true;
        }

        if (isBehindCamera(minecraft, entity, cameraX, cameraY, cameraZ)) {
            return false;
        }

        double occlusionDistance = mode == CullMode.AGGRESSIVE ? AGGRESSIVE_OCCLUSION_DISTANCE : MIN_OCCLUSION_DISTANCE;
        if (mode.atLeast(CullMode.PERFORMANCE) && distanceSq > occlusionDistance * occlusionDistance) {
            return !isOccluded(minecraft, entity, cameraX, cameraY, cameraZ);
        }

        return true;
    }

    private static boolean isProtected(Minecraft minecraft, Entity entity) {
        if (entity.hasCustomName() || entity.isCurrentlyGlowing()) {
            return true;
        }
        if (entity.getBbWidth() > LARGE_ENTITY_SIZE || entity.getBbHeight() > LARGE_ENTITY_SIZE) {
            return true;
        }
        if (entity instanceof Display || entity instanceof LightningBolt || entity instanceof FishingHook) {
            return true;
        }
        return minecraft.player != null && entity.hasIndirectPassenger(minecraft.player);
    }

    private static boolean isHiddenBySettings(Entity entity, PolyOptiConfig config) {
        if (config.hideArmorStands && entity instanceof ArmorStand) {
            return true;
        }
        if (entity instanceof ItemFrame) {
            return config.hideItemFrames || config.hideGlowItemFrames && entity instanceof GlowItemFrame;
        }
        if (config.hideGroundArrows && entity instanceof AbstractArrow arrow) {
            return ((AbstractArrowAccess) arrow).polyopti$isInGround();
        }
        return false;
    }

    private static double maxDistanceSq(Entity entity, PolyOptiConfig config) {
        double categoryLimit = switch (entity) {
            case Player _ -> config.playerDistance;
            case Enemy _ -> config.hostileDistance;
            case LivingEntity _ -> config.passiveDistance;
            default -> config.miscDistance;
        };
        double limit = Math.min(config.entityDistance, categoryLimit);
        return limit * limit;
    }

    private static boolean isBehindCamera(Minecraft minecraft, Entity entity, double cameraX, double cameraY, double cameraZ) {
        Camera camera = minecraft.gameRenderer.mainCamera();
        if (camera == null || !camera.isInitialized()) {
            return false;
        }
        Vector3fc forward = camera.forwardVector();
        double dx = entity.getX() - cameraX;
        double dy = entity.getY() + entity.getBbHeight() * 0.5 - cameraY;
        double dz = entity.getZ() - cameraZ;
        double projection = dx * forward.x() + dy * forward.y() + dz * forward.z();
        double radius = Math.max(entity.getBbWidth(), entity.getBbHeight());
        return projection < -radius - BEHIND_CAMERA_MARGIN;
    }

    private static boolean isOccluded(Minecraft minecraft, Entity entity, double cameraX, double cameraY, double cameraZ) {
        ClientLevel level = minecraft.level;
        if (level == null) {
            return false;
        }

        int entityId = entity.getId();
        byte cached = FrameCache.occlusion(entityId);
        if (cached != FrameCache.UNKNOWN) {
            return cached == FrameCache.OCCLUDED;
        }

        if (!FrameCache.hasOcclusionBudget()) {
            return false;
        }

        boolean occluded = castOcclusionRay(level, entity, cameraX, cameraY, cameraZ);
        FrameCache.putOcclusion(entityId, occluded);
        return occluded;
    }

    private static boolean castOcclusionRay(ClientLevel level, Entity entity, double cameraX, double cameraY, double cameraZ) {
        Vec3 from = new Vec3(cameraX, cameraY, cameraZ);
        Vec3 to = new Vec3(entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ());
        BlockHitResult hit = level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
        if (hit.getType() == HitResult.Type.MISS) {
            return false;
        }
        if (!level.getBlockState(hit.getBlockPos()).canOcclude()) {
            return false;
        }
        double hitDistanceSq = from.distanceToSqr(hit.getLocation());
        return hitDistanceSq + OCCLUSION_TOLERANCE_SQ < from.distanceToSqr(to);
    }

    private static double distanceSq(Entity entity, double cameraX, double cameraY, double cameraZ) {
        double dx = entity.getX() - cameraX;
        double dy = entity.getY() + entity.getBbHeight() * 0.5 - cameraY;
        double dz = entity.getZ() - cameraZ;
        return dx * dx + dy * dy + dz * dz;
    }
}
