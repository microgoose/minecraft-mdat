package net.minecraft.map.data.mca.reader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.InflaterInputStream;

import static net.minecraft.map.data.mca.config.RegionConfig.CHUNK_SIDE;

public class MCARegionReader {
    private final MCAChunkReader MCAChunkReader;

    public MCARegionReader(MCAChunkReader MCAChunkReader) {
        this.MCAChunkReader = MCAChunkReader;
    }

    public void readRegion(ByteBuffer regionBuffer) {
        int[] offsets = parseHeaders(regionBuffer);

        for (int x = 0; x < CHUNK_SIDE; x++) {
            for (int z = 0; z < CHUNK_SIDE; z++) {
                int index = (x % CHUNK_SIDE) + (z % CHUNK_SIDE) * CHUNK_SIDE;
                int offset = offsets[index];
                if (offset == 0) continue;

                int length = regionBuffer.getInt(offset);
                int compressionType = regionBuffer.get(offset + 4);

                if (compressionType != 2) //Zlib
                    throw new RuntimeException("Unknown compression type: " + compressionType);

                byte[] compressedChunkData = new byte[length - 1];
                regionBuffer.get(offset + 5, compressedChunkData);
                byte[] uncompressedChunkData = decompress(compressedChunkData);

                ByteBuffer chunkData = ByteBuffer.wrap(uncompressedChunkData);
                MCAChunkReader.readChunk(chunkData);
            }
        }
    }

    private int[] parseHeaders(ByteBuffer regionBuffer) {
        int[] offsets = new int[1024];
        for (int i = 0; i < 1024; i++) {
            offsets[i] = (regionBuffer.getInt(i * 4) >> 8) * 4096;
        }
        return offsets;
    }

    private byte[] decompress(byte[] compressedData) {
        try (InflaterInputStream inflater = new InflaterInputStream(new ByteArrayInputStream(compressedData))) {
            return inflater.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to decompress chunk", e);
        }
    }
}
