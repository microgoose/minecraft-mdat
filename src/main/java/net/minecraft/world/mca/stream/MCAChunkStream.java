package net.minecraft.world.mca.stream;

import net.minecraft.world.mca.model.MCAChunk;

public interface MCAChunkStream {
    void handleChunk(MCAChunk MCAChunk);
}
