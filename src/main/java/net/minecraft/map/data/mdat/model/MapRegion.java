package net.minecraft.map.data.mdat.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapRegion {
    private final Map<Short, MapChunk> chunks = new HashMap<>();

    public void setChunk(short index, MapChunk chunk) {
        chunks.put(index, chunk);
    }

    public void serialize(DataOutputStream dataStream) throws IOException {
        dataStream.writeShort(chunks.size());

        for (Map.Entry<Short, MapChunk> entry : chunks.entrySet()) {
            dataStream.writeShort(entry.getKey());
            entry.getValue().serialize(dataStream);
        }
    }

    public static MapRegion deserialize(DataInputStream dataStream) throws IOException {
        MapRegion region = new MapRegion();
        int chunkCount = dataStream.readShort();

        for (int i = 0; i < chunkCount; i++) {
            short index = dataStream.readShort();
            MapChunk chunk = MapChunk.deserialize(dataStream);
            region.setChunk(index, chunk);
        }

        return region;
    }
}
