package net.minecraft.world.manager;

import net.minecraft.world.mca.model.MCARegion;

import java.util.LinkedHashMap;
import java.util.Map;

public class RegionCache extends LinkedHashMap<Long, MCARegion> {
    public final int maxRegions;

    public RegionCache(int maxRegions) {
        this.maxRegions = maxRegions;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<Long, MCARegion> eldest) {
        return size() > maxRegions;
    }
}
