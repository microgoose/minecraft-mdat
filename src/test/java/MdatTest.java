import net.minecraft.map.data.mdat.io.RegionSerializer;
import net.minecraft.map.data.mdat.model.MapRegion;
import net.minecraft.map.data.mdat.service.MapRegionService;
import net.minecraft.map.data.mdat.service.MdatService;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class MdatTest {

    public static Path inputPath = Path.of("src/test/resources/r.0.0.mca");
    public static Path inputDirectory = Path.of("C:/Users/mikhail/AppData/Roaming/.minecraft/saves/test/region");
    public static Path outputPath = Path.of("src/test/resources/");

    public static void testSuccessfulConversion(Path inputPath) throws IOException {
        byte[] data = Files.readAllBytes(inputPath);
        ByteBuffer regionBuffer = ByteBuffer.wrap(data);
        regionBuffer.order(ByteOrder.BIG_ENDIAN);

        MapRegion region = MapRegionService.getFromMcaBuffer(regionBuffer);

        String mdatRegionFileName = MdatService.getFileNameByMcaFileName(inputPath.getFileName().toString());
        Path outputFilePath = outputPath.resolve(mdatRegionFileName);

        RegionSerializer.saveToFile(region, outputFilePath);
        MapRegion loadedRegion = RegionSerializer.loadFromFile(outputFilePath);

        Files.delete(outputFilePath);
    }

    public static void testSuccessfulWorldConversion(Path directory) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.mca")) {
            for (Path file : stream) {
                System.out.println("File in work: " + file);
                testSuccessfulConversion(file);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        testSuccessfulConversion(inputPath);
        testSuccessfulWorldConversion(inputDirectory);
    }
}
