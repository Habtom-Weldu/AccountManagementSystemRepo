package app.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class FileManager {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = new FileInputStream("config.properties")) {
            props.load(input);
        } catch (IOException e) {
            System.err.println("⚠️ Failed to load config.properties: " + e.getMessage());
        }
    }

    public static String getAccountsFilePath() {
        String rawPath = props.getProperty("account.file.path", "data/accounts.txt");
        return Paths.get(rawPath).toString();
    }

    public static String getDatabaseFilePath() {
        String rawPath = props.getProperty("accountDatabase.file.path", "data/accountsDB.db");
        return Paths.get(rawPath).toString();
    }

     /*
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
    } */
}
