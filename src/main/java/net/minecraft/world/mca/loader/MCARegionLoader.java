package net.minecraft.world.mca.loader;

import net.minecraft.world.mca.model.MCARegion;
import net.minecraft.world.mca.model.MCAChunk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.InflaterInputStream;

import static net.minecraft.world.mca.config.RegionConfig.CHUNK_SIDE;

public class MCARegionLoader {
    public static MCARegion loadRegion(ByteBuffer regionBuffer, int regionX, int regionZ) {
        int[] offsets = parseHeaders(regionBuffer);
        MCARegion mcaRegion = new MCARegion(regionX, regionZ);

        for (int x = 0; x < CHUNK_SIDE; x++) {
            for (int z = 0; z < CHUNK_SIDE; z++) {
                mcaRegion.setChunk(x, z, loadChunk(regionBuffer, offsets, x, z));
            }
        }

        return mcaRegion;
    }

    public static MCAChunk loadChunk(ByteBuffer regionBuffer, int[] offsets, int localX, int localZ) {
        int index = (localX % CHUNK_SIDE) + (localZ % CHUNK_SIDE) * CHUNK_SIDE;
        int offset = offsets[index];
        if (offset == 0) return null;

        int length = regionBuffer.getInt(offset);
        int compressionType = regionBuffer.get(offset + 4);

        if (compressionType != 2) //Zlib
            throw new RuntimeException("Unknown compression type: " + compressionType);

        byte[] compressedChunkData = new byte[length - 1];
        regionBuffer.get(offset + 5, compressedChunkData);
        byte[] uncompressedChunkData = decompress(compressedChunkData);

        ByteBuffer chunkData = ByteBuffer.wrap(uncompressedChunkData);
        return MCAChunkLoader.loadChunk(chunkData);
    }

    private static int[] parseHeaders(ByteBuffer regionBuffer) {
        if (regionBuffer.remaining() < 4096)
            throw new IllegalArgumentException("Region file header is too small");

        int[] offsets = new int[1024];
        for (int i = 0; i < 1024; i++) {
            offsets[i] = (regionBuffer.getInt(i * 4) >> 8) * 4096;
        }
        return offsets;
    }

    private static byte[] decompress(byte[] compressedData) {
        try (InflaterInputStream inflater = new InflaterInputStream(new ByteArrayInputStream(compressedData));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inflater.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to decompress chunk", e);
        }
    }

}
