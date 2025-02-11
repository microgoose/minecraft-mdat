import net.minecraft.map.data.mdat.service.MdatService;
import net.minecraft.map.data.mdat.io.RegionSerializer;
import net.minecraft.map.data.mdat.model.MapRegion;
import net.minecraft.map.data.mdat.service.MapRegionService;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

public class MdatTest {
    public static Path inputPath = Path.of("src/test/resources/r.0.0.mca");
    public static Path outputPath = Path.of("src/test/resources/");

    public static void testSuccessfulConversion() throws IOException {
        byte[] data = Files.readAllBytes(inputPath);
        ByteBuffer regionBuffer = ByteBuffer.wrap(data);
        regionBuffer.order(ByteOrder.BIG_ENDIAN);

        MapRegion region = MapRegionService.getFromMcaBuffer(regionBuffer);

        String mdatRegionFileName = MdatService.getFileNameByMcaFileName(inputPath.toFile().getName());
        Path outputFilePath = outputPath.resolve(mdatRegionFileName);

        RegionSerializer.saveToFile(region, outputFilePath);
        MapRegion loadedRegion = RegionSerializer.loadFromFile(outputFilePath);

        Files.delete(outputFilePath);
    }

    public static void main(String[] args) throws IOException {
        testSuccessfulConversion();
    }
}