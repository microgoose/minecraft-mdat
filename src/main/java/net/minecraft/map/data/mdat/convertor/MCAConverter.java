package net.minecraft.map.data.mdat.convertor;

import net.minecraft.map.data.config.ChunkConfig;
import net.minecraft.map.data.mca.model.MCAChunk;
import net.minecraft.map.data.mca.model.MCASection;
import net.minecraft.map.data.mdat.model.MapChunk;

public class MCAConverter {
    public static MapChunk convertChunk(MCAChunk mcaChunk) {
        MapChunk mapChunk = new MapChunk();

        for (int chunkXIndex = 0; chunkXIndex < ChunkConfig.BLOCKS_SIDE; chunkXIndex++) {
            for (int chunkYIndex = 0; chunkYIndex < ChunkConfig.BLOCKS_SIDE; chunkYIndex++) {
                processSections(chunkXIndex, chunkYIndex, mapChunk, mcaChunk.sections);
            }
        }

        return mapChunk;
    }

    public static void processSections(int chunkXIndex, int chunkYIndex, MapChunk mapChunk, MCASection[] sections) {
        for (int i = 0; i < sections.length; i++) {
            MCASection section = sections[i];
            short startHeight = (short) (section.y * ChunkConfig.BLOCKS_SIDE);
            short endHeight = (short) (startHeight - ChunkConfig.BLOCKS_SIDE);

            if (section.palette.size() == 1) {
                if (isTransparent(section.palette.getFirst()))
                    return;

                mapChunk.setBlockData(chunkXIndex, chunkYIndex, startHeight, section.palette.getFirst());
                return;
            }

            for (short height = startHeight; height > endHeight; height--) {
                String blockType = section.getBlockType(chunkXIndex, chunkYIndex, height);

                if (isTransparent(blockType)) continue;

                mapChunk.setBlockData(chunkXIndex, chunkYIndex, height, blockType);
                return;
            }
        }
    }

    private static boolean isTransparent(String blockType) {
        return blockType.endsWith("air");
    }
}
