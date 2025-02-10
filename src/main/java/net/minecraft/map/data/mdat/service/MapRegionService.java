package net.minecraft.map.data.mdat.service;

import net.minecraft.map.data.mca.reader.MCAChunkReader;
import net.minecraft.map.data.mca.reader.MCARegionReader;
import net.minecraft.map.data.mca.reader.MCASectionReader;
import net.minecraft.map.data.mdat.io.RegionSurfaceScanner;
import net.minecraft.map.data.mdat.model.MapRegion;

import java.nio.ByteBuffer;

public class MapRegionService {
    public static MapRegion getFromMcaBuffer(ByteBuffer mcaBuffer) {
        MapRegion region = new MapRegion();
        RegionSurfaceScanner regionSurfaceScanner = new RegionSurfaceScanner(region);

        MCASectionReader mcaSectionReader = new MCASectionReader();
        MCAChunkReader mcaChunkReader = new MCAChunkReader(mcaSectionReader, regionSurfaceScanner);
        MCARegionReader mcaRegionReader = new MCARegionReader(mcaChunkReader);

        mcaRegionReader.readRegion(mcaBuffer);
        return region;
    }
}
