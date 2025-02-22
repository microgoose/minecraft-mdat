package net.minecraft.world.locator;

import net.minecraft.world.manager.WorldManager;
import net.minecraft.world.mca.model.MCAChunk;

public class ChunkLocator {
    private final WorldManager wm;

    public ChunkLocator(WorldManager wm) {
        this.wm = wm;
    }

    public MCAChunk getChunkByBlock(int blockX, int blockZ) {
        int chunkX = blockX >> 4;
        int chunkZ = blockZ >> 4;
        return wm.loadChunk(chunkX, chunkZ);
    }

    public MCAChunk getChunk(int chunkX, int chunkZ) {
        return wm.loadChunk(chunkX, chunkZ);
    }
}
