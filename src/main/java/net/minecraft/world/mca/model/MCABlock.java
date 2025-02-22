package net.minecraft.world.mca.model;

public class MCABlock {
    public final int x, y, z;
    public final String type;

    public MCABlock(int x, int y, int z, String type) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
    }
}

