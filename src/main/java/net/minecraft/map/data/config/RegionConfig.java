package net.minecraft.map.data.config;

public class RegionConfig {
    public final static int CHUNK_SIDE = 32;
    public final static int CHUNKS_COUNT = CHUNK_SIDE * CHUNK_SIDE; //1024
    public final static int BLOCKS_SIDE = CHUNK_SIDE * ChunkConfig.BLOCKS_SIDE; //512
}
