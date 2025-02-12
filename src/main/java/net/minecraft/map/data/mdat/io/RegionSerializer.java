package net.minecraft.map.data.mdat.io;

import net.minecraft.map.data.mdat.model.MapRegion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

public class RegionSerializer {

    public static void saveToFile(MapRegion region, Path path) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024 * 2); // 2 MB
        buffer.order(ByteOrder.BIG_ENDIAN);
        region.serialize(buffer);
        buffer.flip();
        byteArrayOutputStream.write(buffer.array(), buffer.position(), buffer.remaining());
        Files.write(path, byteArrayOutputStream.toByteArray());
    }

    public static MapRegion loadFromFile(Path path) throws IOException {
        byte[] data = Files.readAllBytes(path);
        ByteBuffer regionBuffer = ByteBuffer.wrap(data);
        regionBuffer.order(ByteOrder.BIG_ENDIAN);
        return MapRegion.deserialize(regionBuffer);
    }
}

