package app.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import app.util.FileManager;

public class DatabaseManager {
    private static final String DB_URL;

    static {
        String dbFilePath = FileManager.getDatabaseFilePath();
        DB_URL = "jdbc:sqlite:" + dbFilePath;
    }

    public static Connection getDatabaseConnection() {
        // check for SQLite JDBC driver exist
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ SQLite JDBC driver not found. Please add sqlite-jdbc.jar to classpath.");
            //throw new RuntimeException(e);
            System.exit(1); // Exit the program with error status
        }

        try {
            return DriverManager.getConnection(DB_URL); // creates Database if not existed
        } catch (SQLException e) {
            System.err.println("❌ Failed to connect to DB: " + e.getMessage());
            return null;
        }
    }
    /*
    // This class will handle connecting to SQLite app.database file.
    // private static final String DB_URL = "jdbc:sqlite:accountsDB.db"; // stored in project root
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
    } */
}
