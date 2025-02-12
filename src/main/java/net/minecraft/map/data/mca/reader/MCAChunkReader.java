package net.minecraft.map.data.mca.reader;

import net.minecraft.map.data.mca.model.MCAChunk;
import net.minecraft.map.data.mca.model.MCASection;
import net.minecraft.map.data.mca.stream.MCAChunkStream;
import net.minecraft.map.data.nbt.NBTReader;
import net.minecraft.map.data.nbt.NBTSkipper;

import java.nio.ByteBuffer;

public class MCAChunkReader {
    private final MCASectionReader mcaSectionReader;
    private final MCAChunkStream mcaChunkStream;

    public MCAChunkReader(MCASectionReader MCASectionReader, MCAChunkStream MCAChunkStream) {
        this.mcaSectionReader = MCASectionReader;
        this.mcaChunkStream = MCAChunkStream;
    }

    public void readChunk(ByteBuffer chunkData) {
        byte rootTagType = chunkData.get();
        if (rootTagType != 0x0A)
            throw new IllegalStateException("Invalid NBT chunk format: root is not TAG_Compound");

        NBTReader.readString(chunkData);

        Integer chunkXPos = null;
        Integer chunkZPos = null;
        MCASection[] chunkMCASections = null;

        while (chunkData.hasRemaining()) {
            byte tagType = chunkData.get();
            if (tagType == 0) break;
            String tagName = NBTReader.readTagName(chunkData);

            switch (tagName) {
                case "xPos":
                    chunkXPos = chunkData.getInt();
                    continue;
                case "zPos":
                    chunkZPos = chunkData.getInt();
                    continue;
                case "sections":
                    chunkMCASections = readSections(chunkData);
                    continue;
            }

            NBTSkipper.skipTag(chunkData, tagType);
        }

        if (chunkXPos == null)
            throw new IllegalStateException("Chunk X position not initialized");
        if (chunkZPos == null)
            throw new IllegalStateException("Chunk Z position not initialized");
        if (chunkMCASections == null)
            throw new IllegalStateException("Chunk sections not initialized");

        mcaChunkStream.handleChunk(new MCAChunk(chunkXPos, chunkZPos, chunkMCASections));
    }

    public MCASection[] readSections(ByteBuffer sectionsData) {
        byte sectionsTagType = sectionsData.get();
        int sectionsCount = sectionsData.getInt();

        if (sectionsTagType != 10)
            throw new IllegalStateException("Invalid section tag type: " + sectionsTagType);

        MCASection[] sectionArray = new MCASection[256];
        byte minY = Byte.MAX_VALUE, maxY = Byte.MIN_VALUE;

        for (int i = 0; i < sectionsCount; i++) {
            MCASection newMCASection = mcaSectionReader.readSection(sectionsData);

            if (!newMCASection.palette.isEmpty()) {
                int index = newMCASection.y & 0xFF;

                sectionArray[index] = newMCASection;

                if (newMCASection.y < minY) minY = newMCASection.y;
                if (newMCASection.y > maxY) maxY = newMCASection.y;
            }
        }

        MCASection[] sectionSortedArray = new MCASection[sectionsCount];
        for (byte i = maxY, j = 0; i > minY; i--) {
            int index = i & 0xFF;
            if (sectionArray[index] == null)
                continue;

            sectionSortedArray[j++] = sectionArray[index];
        }

        return sectionSortedArray;
    }
}
