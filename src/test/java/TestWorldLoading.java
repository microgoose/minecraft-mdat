import net.minecraft.world.locator.BlockLocator;
import net.minecraft.world.locator.ChunkLocator;
import net.minecraft.world.locator.RegionLocator;
import net.minecraft.world.manager.WorldManager;
import net.minecraft.world.mca.model.MCABlock;
import net.minecraft.world.mca.model.MCAChunk;
import net.minecraft.world.mca.model.MCARegion;

import java.nio.file.Path;

public class TestWorldLoading {
    public static void main(String[] args) {
        Path worldPath = Path.of("C:\\Users\\mikhail\\AppData\\Roaming\\.minecraft\\saves\\test");
        WorldManager manager = new WorldManager(worldPath);
        BlockLocator blockLocator = new BlockLocator(manager);
        ChunkLocator chunkLocator = new ChunkLocator(manager);
        RegionLocator regionLocator = new RegionLocator(manager);

        MCABlock block = blockLocator.getBlock(0,0,0);
        MCAChunk chunk = chunkLocator.getChunk(0,0);
        MCARegion region = regionLocator.getRegion(0,0);

        System.out.printf("%s; %s; %s; %s%n", block.x, block.y, block.z, block.type);
        System.out.printf("%s; %s; %s; %n", chunk.x, chunk.z, chunk.sections.length);
        System.out.printf("%s; %s; %s; %n", region.x, region.z, region.chunks.length);
    }
}
