package app;

import java.io.*;
public class FileManager {

    // Folder and file name constants
    private static final String DATA_FOLDER = "data";
    private static final String FILE_NAME = "Accounts.txt";

    // Public method to get the full file path
    public static String getFilePath() {
        createDataFolderIfNeeded();
        return DATA_FOLDER + File.separator + FILE_NAME;
    }

    // Private helper method to create the data folder if it doesn't exist
    private static void createDataFolderIfNeeded() {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            boolean created = folder.mkdir();
            if (created) {
                System.out.println("✅ Data folder created.");
            } else {
                System.out.println("⚠️ Failed to create data folder.");
            }
        }
    }
}
