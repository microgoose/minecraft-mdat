package net.minecraft.map.data.mca.stream;

import net.minecraft.map.data.mca.model.MCAChunk;

public interface MCAChunkStream {
    void handleChunk(MCAChunk MCAChunk);
}
