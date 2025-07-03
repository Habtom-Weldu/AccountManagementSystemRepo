package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    // This class will handle connecting to SQLite database file.
    //private static final String DB_URL = "jdbc:sqlite:accountsDB.db"; // stored in project root
    private static final String DB_FILE_PATH = "data/accountsDB.db";

    public static Connection getConnection() throws SQLException {
        try {
            // Ensure the 'data' folder exists
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            // Build the correct SQLite connection string
            String url = "jdbc:sqlite:" + DB_FILE_PATH;
            System.out.println("📍 Connecting to DB at: " + new File(DB_FILE_PATH).getAbsolutePath());

            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.err.println("❌ Failed to connect to DB: " + e.getMessage());
            return null;
        }
    }
}
