package net.minecraft.map.data.mdat.model;

import net.minecraft.map.data.config.ChunkConfig;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MapChunk {
    private final List<String> blockTypesPalette = new ArrayList<>();
    private final List<Short> heightsPalette = new ArrayList<>();
    private final byte[] heights = new byte[ChunkConfig.BLOCKS_COUNT];
    private final byte[] blockTypes = new byte[ChunkConfig.BLOCKS_COUNT];

    public void setBlockData(int chunkBlockX, int chunkBlockY, short height, String blockType) {
        int index = getIndex(chunkBlockX, chunkBlockY);

        int btpIndex = heightsPalette.indexOf(height);
        if (btpIndex == -1) {
            heightsPalette.add(height);
            btpIndex = heightsPalette.size() - 1;
        }

        int hpIndex = blockTypesPalette.indexOf(blockType);
        if (hpIndex == -1) {
            blockTypesPalette.add(blockType);
            hpIndex = blockTypesPalette.size() - 1;
        }

        blockTypes[index] = (byte) btpIndex;
        heights[index] = (byte) hpIndex;
    }

    private int getIndex(int chunkBlockX, int chunkBlockY) {
        return chunkBlockY * ChunkConfig.BLOCKS_SIDE + chunkBlockX;
    }

    public void serialize(ByteBuffer buffer) {
        buffer.put((byte) blockTypesPalette.size());
        for (String blockType : blockTypesPalette) {
            byte[] strBytes = blockType.getBytes(StandardCharsets.UTF_8);
            buffer.putShort((short) strBytes.length);
            buffer.put(strBytes);
        }

        buffer.put((byte) heightsPalette.size());
        for (short height : heightsPalette) {
            buffer.putShort(height);
        }

        for (byte blockTypeIndex : blockTypes) {
            buffer.put(blockTypeIndex);
        }

        for (byte heightIndex : heights) {
            buffer.put(heightIndex);
        }
    }

    public static MapChunk deserialize(ByteBuffer buffer) {
        MapChunk chunk = new MapChunk();

        int blockTypesSize = buffer.get();
        for (int i = 0; i < blockTypesSize; i++) {
            short strLength = buffer.getShort();
            byte[] strBytes = new byte[strLength];
            buffer.get(strBytes);
            chunk.blockTypesPalette.add(new String(strBytes, StandardCharsets.UTF_8));
        }

        byte heightsSize = buffer.get();
        for (int i = 0; i < heightsSize; i++) {
            chunk.heightsPalette.add(buffer.getShort());
        }

        for (int i = 0; i < ChunkConfig.BLOCKS_COUNT; i++) {
            chunk.blockTypes[i] = buffer.get();
        }

        for (int i = 0; i < ChunkConfig.BLOCKS_COUNT; i++) {
            chunk.heights[i] = buffer.get();
        }

        return chunk;
    }
}
