package net.minecraft.map.data.mca.reader;

import net.minecraft.map.data.mca.stream.MCAChunkStream;
import net.minecraft.map.data.nbt.NBTReader;
import net.minecraft.map.data.nbt.NBTSkipper;
import net.minecraft.map.data.mca.model.MCAChunk;
import net.minecraft.map.data.mca.model.MCASection;

import java.nio.ByteBuffer;

import static net.minecraft.map.data.mca.config.SectionConfig.CHUNK_SECTION_COUNT;
import static net.minecraft.map.data.mca.config.SectionConfig.SECTION_MIN_Y;

public class MCAChunkReader {
    private final MCASectionReader MCASectionReader;
    private final net.minecraft.map.data.mca.stream.MCAChunkStream MCAChunkStream;

    public MCAChunkReader(MCASectionReader MCASectionReader, MCAChunkStream MCAChunkStream) {
        this.MCASectionReader = MCASectionReader;
        this.MCAChunkStream = MCAChunkStream;
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

        MCAChunkStream.handleChunk(new MCAChunk(chunkXPos, chunkZPos, chunkMCASections));
    }

    public MCASection[] readSections(ByteBuffer sectionsData) {
        byte sectionsTagType = sectionsData.get();
        int sectionsCount = sectionsData.getInt();

        if (sectionsTagType != 10)
            throw new IllegalStateException("Invalid section tag type: " + sectionsTagType);

        MCASection[] MCASections = new MCASection[CHUNK_SECTION_COUNT];
        for (int i = 0; i < sectionsCount; i++) {
            MCASection newMCASection = MCASectionReader.readSection(sectionsData);
            MCASections[(CHUNK_SECTION_COUNT - 1) - (newMCASection.y - SECTION_MIN_Y)] = newMCASection;
        }

        return MCASections;
    }
}
