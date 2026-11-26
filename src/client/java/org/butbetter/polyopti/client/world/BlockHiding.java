package org.butbetter.polyopti.client.world;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerBedBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.butbetter.polyopti.client.config.PolyOptiConfig;

public final class BlockHiding {

    private static final Map<BlockState, BlockStateModel> HIDDEN_MODELS = new ConcurrentHashMap<>();

    private static volatile Set<Block> hiddenBlocks = Set.of();
    private static volatile boolean active;

    private BlockHiding() {
    }

    public static void refresh() {
        PolyOptiConfig config = PolyOptiConfig.INSTANCE;
        Set<Block> blocks = new HashSet<>();
        boolean enabled = config.enabled;

        if (enabled && config.hideTallGrass) {
            blocks.add(Blocks.SHORT_GRASS);
            blocks.add(Blocks.TALL_GRASS);
        }

        if (enabled && config.hideFerns) {
            blocks.add(Blocks.FERN);
            blocks.add(Blocks.LARGE_FERN);
            blocks.add(Blocks.DEAD_BUSH);
            blocks.add(Blocks.SUGAR_CANE);
        }

        hiddenBlocks = blocks.isEmpty() ? Set.of() : Set.copyOf(blocks);
        active = !hiddenBlocks.isEmpty() || hasFamilyHiding(config);
        HIDDEN_MODELS.clear();
    }

    public static BlockStateModel hiddenModel(BlockState state, BlockStateModel original) {
        if (!active || original == null || !matches(state)) {
            return original;
        }
        return HIDDEN_MODELS.computeIfAbsent(state, _ -> new HiddenStateModel(original));
    }

    private static boolean hasFamilyHiding(PolyOptiConfig config) {
        return config.enabled && (config.hideFlowers || config.hideFences || config.hideGates || config.hideHeads);
    }

    private static boolean matches(BlockState state) {
        Block block = state.getBlock();
        if (hiddenBlocks.contains(block)) {
            return true;
        }

        PolyOptiConfig config = PolyOptiConfig.INSTANCE;
        if (config.hideFlowers && isFlower(block)) {
            return true;
        }
        if (config.hideFences && block instanceof FenceBlock) {
            return true;
        }
        if (config.hideGates && block instanceof FenceGateBlock) {
            return true;
        }
        return config.hideHeads && block instanceof AbstractSkullBlock;
    }

    private static boolean isFlower(Block block) {
        return block instanceof FlowerBlock || block instanceof TallFlowerBlock || block instanceof FlowerBedBlock;
    }

    private static final class HiddenStateModel implements BlockStateModel {

        private final BlockStateModel delegate;

        private HiddenStateModel(BlockStateModel delegate) {
            this.delegate = delegate;
        }

        @Override
        public void collectParts(RandomSource random, List<BlockStateModelPart> parts) {
        }

        @Override
        public Material.Baked particleMaterial() {
            return delegate.particleMaterial();
        }

        @Override
        public int materialFlags() {
            return delegate.materialFlags();
        }
    }
}
