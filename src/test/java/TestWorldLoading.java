import net.minecraft.world.mca.manager.MCARegionManager;
import net.minecraft.world.mca.model.MCARegion;

import java.io.IOException;
import java.nio.file.Path;

public class TestWorldLoading {
    public static void main(String[] args) throws IOException {
        Path worldPath = Path.of("C:\\Users\\mikhail\\AppData\\Roaming\\.minecraft\\saves\\test");
        MCARegionManager manager = new MCARegionManager(worldPath);

        MCARegion region = manager.loadRegion(0,0);
        System.out.println("Done");
    }
}
