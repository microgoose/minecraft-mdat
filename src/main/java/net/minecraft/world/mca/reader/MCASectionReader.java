package net.minecraft.world.mca.reader;

import net.minecraft.world.mca.model.MCASection;
import net.minecraft.world.nbt.NBTNavigator;
import net.minecraft.world.nbt.NBTReader;
import net.minecraft.world.nbt.NBTSkipper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MCASectionReader {
    public MCASection readSection(ByteBuffer sectionData) {
        byte sectionY = Byte.MIN_VALUE;
        List<String> sectionPalette = new ArrayList<>();
        long[] sectionBlocks = null;

        while (sectionData.hasRemaining()) {
            byte tagType = sectionData.get();
            if (tagType == 0) break;
            String tagName = NBTReader.readTagName(sectionData);

            if (tagName.equals("Y")) {
                sectionY = sectionData.get();
                continue;
            }

            if (tagName.equals("block_states")) {
                while (sectionData.hasRemaining()) {
                    byte subTagType = sectionData.get();
                    if (subTagType == 0) break;
                    String subTagName = NBTReader.readTagName(sectionData);

                    if (subTagName.equals("palette")) {
                        sectionPalette = readPalette(sectionData);
                        continue;
                    }

                    if (subTagName.equals("data")) {
                        sectionBlocks = NBTReader.readLongArray(sectionData);
                        continue;
                    }

                    NBTSkipper.skipTag(sectionData, subTagType);
                }

                continue;
            }

            NBTSkipper.skipTag(sectionData, tagType);
        }

        if (sectionPalette.size() > 1 && sectionBlocks == null)
            throw new IllegalStateException("Section Blocks not initialized");

        return new MCASection(sectionY, sectionPalette, sectionBlocks);
    }

    private List<String> readPalette(ByteBuffer palettePos) {
        palettePos.get();
        int paletteCount = palettePos.getInt();
        List<String> palette = new ArrayList<>();

        for (int i = 0; i < paletteCount; i++) {
            NBTNavigator.moveToTag(palettePos, 8, "Name");
            palette.add(NBTReader.readString(palettePos));
            NBTSkipper.skipCompound(palettePos);
        }

        return palette;
    }
}
