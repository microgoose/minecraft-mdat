package net.minecraft.world.locator;

import net.minecraft.world.manager.WorldManager;
import net.minecraft.world.mca.model.MCABlock;
import net.minecraft.world.mca.model.MCAChunk;
import net.minecraft.world.mca.model.MCARegion;
import net.minecraft.world.mca.model.MCASection;

import static net.minecraft.world.mca.config.ChunkConfig.BLOCKS_SIDE;

public class BlockLocator {
    private final WorldManager wm;

    public BlockLocator(WorldManager wm) {
        this.wm = wm;
    }

    public MCABlock getBlock(int x, int y, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        int regionX = chunkX >> 5;
        int regionZ = chunkZ >> 5;

        MCARegion region = wm.loadRegion(regionX, regionZ);
        if (region == null) return null;

        MCAChunk chunk = region.getChunk(chunkX & 31, chunkZ & 31);
        if (chunk == null) return null;

        int localX = x & 15;
        int localZ = z & 15;

        MCASection[] sections = chunk.sections;
        for (int i = sections.length - 1; i >= 0; i--) {
            MCASection section = sections[i];
            int sectionY = section.y * BLOCKS_SIDE;

            if (y >= sectionY && y < sectionY + BLOCKS_SIDE) {
                String blockType = section.getBlockType(localX, y, localZ);
                return new MCABlock(x, y, z, blockType);
            }
        }

        return null;
    }
}