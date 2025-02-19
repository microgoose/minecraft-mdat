package net.minecraft.world.mca.model;

import static net.minecraft.world.mca.config.RegionConfig.CHUNK_SIDE;

public class MCARegion {
    private final int x;
    private final int z;
    private final MCAChunk[] chunks = new MCAChunk[CHUNK_SIDE * CHUNK_SIDE];

    public MCARegion(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public void setChunk(int chunkX, int chunkZ, MCAChunk chunk) {
        chunks[getChunkIndex(chunkX, chunkZ)] = chunk;
    }

    public MCAChunk getChunk(int chunkX, int chunkZ) {
        return chunks[getChunkIndex(chunkX, chunkZ)];
    }

    public MCAChunk[] getChunks() {
        return chunks;
    }

    private static int getChunkIndex(int chunkX, int chunkZ) {
        return (chunkX & 31) + (chunkZ & 31) * 32;
    }
}