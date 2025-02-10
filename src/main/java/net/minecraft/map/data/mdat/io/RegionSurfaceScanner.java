package net.minecraft.map.data.mdat.io;

import net.minecraft.map.data.mca.model.MCAChunk;
import net.minecraft.map.data.mca.stream.MCAChunkStream;
import net.minecraft.map.data.mdat.convertor.MCAConverter;
import net.minecraft.map.data.mdat.model.MapChunk;
import net.minecraft.map.data.mdat.model.MapRegion;
import net.minecraft.map.data.config.RegionConfig;

public class RegionSurfaceScanner implements MCAChunkStream {
    private final MapRegion region;

    public RegionSurfaceScanner(MapRegion region) {
        this.region = region;
    }

    @Override
    public void handleChunk(MCAChunk mcaChunk) {
        int regionXIndex = Math.abs(mcaChunk.x) % RegionConfig.CHUNK_SIDE;
        int regionYIndex = Math.abs(mcaChunk.z) % RegionConfig.CHUNK_SIDE;
        short chunkRegionIndex = (short) (regionYIndex * RegionConfig.CHUNK_SIDE + regionXIndex);

        MapChunk mapChunk = MCAConverter.convertChunk(mcaChunk);
        region.setChunk(chunkRegionIndex, mapChunk);
    }
}
