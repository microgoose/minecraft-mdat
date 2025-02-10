package net.minecraft.map.data.mdat.model;

import net.minecraft.map.data.config.ChunkConfig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

    public void serialize(DataOutputStream dataStream) throws IOException {
        dataStream.writeByte(blockTypesPalette.size());
        for (String blockType : blockTypesPalette) {
            byte[] strBytes = blockType.getBytes(StandardCharsets.UTF_8);
            dataStream.writeByte(strBytes.length);
            dataStream.write(strBytes);
        }

        dataStream.writeByte(heightsPalette.size());
        for (short height : heightsPalette) {
            dataStream.writeShort(height);
        }

        for (short blockTypeIndex : blockTypes) {
            dataStream.writeByte(blockTypeIndex);
        }

        for (short heightIndex : heights) {
            dataStream.writeByte(heightIndex);
        }
    }

    public static MapChunk deserialize(DataInputStream in) throws IOException {
        MapChunk chunk = new MapChunk();

        int blockTypesSize = in.readByte();
        for (int i = 0; i < blockTypesSize; i++) {
            int strLength = in.readByte();
            byte[] strBytes = new byte[strLength];
            in.readFully(strBytes);
            chunk.blockTypesPalette.add(new String(strBytes, StandardCharsets.UTF_8));
        }

        int heightsSize = in.readByte();
        for (int i = 0; i < heightsSize; i++) {
            chunk.heightsPalette.add(in.readShort());
        }

        for (int i = 0; i < ChunkConfig.BLOCKS_COUNT; i++) {
            chunk.blockTypes[i] = in.readByte();
        }

        for (int i = 0; i < ChunkConfig.BLOCKS_COUNT; i++) {
            chunk.heights[i] = in.readByte();
        }

        return chunk;
    }
}
