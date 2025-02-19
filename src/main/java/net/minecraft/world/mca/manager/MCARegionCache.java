package net.minecraft.world.mca.manager;

import net.minecraft.world.mca.model.MCARegion;

import java.util.LinkedHashMap;
import java.util.Map;

public class MCARegionCache extends LinkedHashMap<Long, MCARegion> {
    public final int maxRegions;

    public MCARegionCache(int maxRegions) {
        this.maxRegions = maxRegions;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<Long, MCARegion> eldest) {
        return size() > maxRegions;
    }
}
