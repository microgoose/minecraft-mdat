package net.minecraft.map.data.mca.model;

import net.minecraft.map.data.mca.utils.MCAMath;

import java.util.List;

public class MCASection {
    public byte y;
    public List<String> palette;
    public long[] blocks;
    public int bitsPerBlock;

    public MCASection(byte y, List<String> palette, long[] blocks) {
        this.y = y;
        this.palette = palette;
        this.blocks = blocks;
        this.bitsPerBlock = (blocks == null || blocks.length == 0) ? -1 : (blocks.length * 64 / 4096);
    }

    public String getBlockType(int x, int y, int z) {
        if (blocks == null || bitsPerBlock <= 0) return palette.getFirst();
        int blockIndex = ((y & 0xF) << 8) + ((z & 0xF) << 4) + (x & 0xF);
        long value = MCAMath.getValueFromLongArray(blocks, blockIndex, bitsPerBlock);
        return palette.get((int) value);
    }
}
