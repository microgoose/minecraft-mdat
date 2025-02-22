package net.minecraft.world.manager;

import net.minecraft.world.mca.loader.MCARegionLoader;
import net.minecraft.world.mca.model.MCAChunk;
import net.minecraft.world.mca.model.MCARegion;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class WorldManager {
    private final Path worldPath;
    private final RegionCache regionCache;

    public WorldManager(Path worldPath) {
        this.worldPath = worldPath;
        regionCache = new RegionCache(CacheConfig.MAX_REGIONS);
    }

    public WorldManager(Path worldPath, RegionCache regionCache) {
        this.worldPath = worldPath;
        this.regionCache = regionCache;
    }

    public MCARegion loadRegion(int regionX, int regionZ) {
        long regionKey = getRegionKey(regionX, regionZ);
        MCARegion region = regionCache.get(regionKey);

        if (region != null) {
            return region;
        }

        Path regionPath = getRegionPath(regionX, regionZ);

        try (FileChannel channel = FileChannel.open(regionPath, StandardOpenOption.READ)) {
            //memory trash
            MappedByteBuffer regionBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            regionBuffer.order(ByteOrder.BIG_ENDIAN);

            region = MCARegionLoader.loadRegion(regionBuffer, regionX, regionZ);
            regionCache.put(regionKey, region);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return region;
    }

    public MCAChunk loadChunk(int chunkX, int chunkZ) {
        int regionX = chunkX >> 5;
        int regionZ = chunkZ >> 5;
        int localChunkX = chunkX & 31;
        int localChunkZ = chunkZ & 31;
        MCARegion region = loadRegion(regionX, regionZ);
        return region.getChunk(localChunkX, localChunkZ);
    }

    private Path getRegionPath(int regionX, int regionZ) {
        return worldPath.resolve("region/r." + regionX + "." + regionZ + ".mca");
    }

    private long getRegionKey(int regionX, int regionZ) {
        return (long) regionX << 32 | (regionZ & 0xFFFFFFFFL);
    }
}
