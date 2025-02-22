package net.minecraft.world.mca.model;

public class MCAChunk {
    public final int x;
    public final int z;
    public final MCASection[] sections;

    public MCAChunk(int x, int z, MCASection[] sections) {
        this.x = x;
        this.z = z;
        this.sections = sections;
    }
}
