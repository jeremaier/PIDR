package src.util;

import java.io.*;

public class FileManager {
    public static String getFilePath(String name) {
        String directoryPath = System.getProperty("user.dir") + File.separator + "saves";
        File folder = new File(directoryPath);

        if (!folder.exists())
            folder.mkdirs();

        return folder.getAbsoluteFile() + File.separator + name;
    }
}