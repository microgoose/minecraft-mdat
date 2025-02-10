package net.minecraft.map.data.mca.model;

public class MCAChunk {
    public int x;
    public int z;
    public MCASection[] sections;

    public MCAChunk(int x, int z, MCASection[] sections) {
        this.x = x;
        this.z = z;
        this.sections = sections;
    }
}
