package org.butbetter.polyopti.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import org.butbetter.polyopti.client.config.PolyOptiConfig;

public final class SoundPolicy {

    private SoundPolicy() {
    }

    public static boolean shouldSkip(SoundInstance sound) {
        PolyOptiConfig config = PolyOptiConfig.INSTANCE;
        if (!config.enabled || !config.limitSoundDistance || sound.isRelative() || sound.isLooping()) {
            return false;
        }

        SoundSource source = sound.getSource();
        if (source == SoundSource.MUSIC || source == SoundSource.AMBIENT || source == SoundSource.VOICE || source == SoundSource.UI) {
            return false;
        }

        Entity camera = Minecraft.getInstance().getCameraEntity();
        if (camera == null) {
            return false;
        }

        double limit = config.soundDistance;
        double dx = sound.getX() - camera.getX();
        double dy = sound.getY() - camera.getY();
        double dz = sound.getZ() - camera.getZ();
        return dx * dx + dy * dy + dz * dz > limit * limit;
    }
}
