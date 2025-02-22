package net.minecraft.world.mca.model;

import static net.minecraft.world.mca.config.RegionConfig.CHUNK_SIDE;

public class MCARegion {
    public final int x;
    public final int z;
    public final MCAChunk[] chunks = new MCAChunk[CHUNK_SIDE * CHUNK_SIDE];

    public MCARegion(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public void setChunk(int chunkX, int chunkZ, MCAChunk chunk) {
        chunks[getChunkIndex(chunkX, chunkZ)] = chunk;
    }

    public MCAChunk getChunk(int chunkX, int chunkZ) {
        return chunks[getChunkIndex(chunkX, chunkZ)];
    }

    private static int getChunkIndex(int chunkX, int chunkZ) {
        return (chunkX & 31) + (chunkZ & 31) * 32;
    }
}