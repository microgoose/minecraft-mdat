package net.minecraft.map.data.mdat.service;

public class MdatService {
    public static String getFileNameByMcaFileName(String mcaFileName) {
        return String.format("%s.mdat", mcaFileName.replaceFirst("[.][^.]+$", ""));
    }
}
