package net.minecraft.map.data.mdat.io;

import net.minecraft.map.data.mdat.model.MapRegion;

import java.io.*;
import java.nio.file.Path;

public class RegionSerializer {

    public static void saveToFile(MapRegion region, Path path) throws IOException {
        File file = path.toFile();

        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            region.serialize(out);
        }
    }

    public static MapRegion loadFromFile(Path path) throws IOException {
        File file = path.toFile();

        if (!file.exists()) {
            throw new FileNotFoundException("File doesn't exist: " + file.getPath());
        }

        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            return MapRegion.deserialize(in);
        }
    }
}
