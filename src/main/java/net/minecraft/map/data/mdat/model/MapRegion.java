package net.minecraft.map.data.mdat.model;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class MapRegion {
    private final Map<Short, MapChunk> chunks = new HashMap<>();

    public void setChunk(short index, MapChunk chunk) {
        chunks.put(index, chunk);
    }

    public void serialize(ByteBuffer buffer) {
        buffer.putShort((short) chunks.size());

        for (Map.Entry<Short, MapChunk> entry : chunks.entrySet()) {
            buffer.putShort(entry.getKey());
            entry.getValue().serialize(buffer);
        }
    }

    public static MapRegion deserialize(ByteBuffer buffer) {
        MapRegion region = new MapRegion();
        int chunkCount = buffer.getShort();

        for (int i = 0; i < chunkCount; i++) {
            short index = buffer.getShort();
            MapChunk chunk = MapChunk.deserialize(buffer);
            region.setChunk(index, chunk);
        }

        return region;
    }
}
