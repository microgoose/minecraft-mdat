package net.minecraft.world.mca.model;

public class MCABlock {
    public int x;
    public int y;
    public int z;
    public String type;

    public MCABlock(int x, int y, int z, String type) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
    }
}
