package net.minecraft.map.data.mdat.convertor;

import net.minecraft.map.data.config.ChunkConfig;
import net.minecraft.map.data.mca.model.MCAChunk;
import net.minecraft.map.data.mca.model.MCASection;
import net.minecraft.map.data.mdat.model.MapChunk;

import java.util.List;

public class MCAConverter {
    public static MapChunk convertChunk(MCAChunk mcaChunk) {
        MapChunk mapChunk = new MapChunk();
        MCASection[] sections = mcaChunk.sections;

        for (int i = 0; i < sections.length; i++) {
            MCASection section = sections[i];
            List<String> palette = section.palette;

            if (palette.size() == 1 && palette.getFirst().equals("minecraft:air"))
                continue;

            processSection(mapChunk, section);
        }

        return mapChunk;
    }

    public static void processSection(MapChunk mapChunk, MCASection section) {
        short startHeight = (short) (section.y * ChunkConfig.BLOCKS_SIDE);
        short endHeight = (short) (startHeight - ChunkConfig.BLOCKS_SIDE);

        for (int chunkXIndex = 0; chunkXIndex < ChunkConfig.BLOCKS_SIDE; chunkXIndex++) {
            for (int chunkYIndex = 0; chunkYIndex < ChunkConfig.BLOCKS_SIDE; chunkYIndex++) {
                for (short height = startHeight; height > endHeight; height--) {
                    String blockType = section.getBlockType(chunkXIndex, chunkYIndex, height);

                    if (isTransparent(blockType)) continue;

                    mapChunk.setBlockData(chunkXIndex, chunkYIndex, height, blockType);
                    break;
                }
            }
        }
    }

    private static boolean isTransparent(String blockType) {
        return blockType.endsWith("air");
    }
}
