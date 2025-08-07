package app.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import app.util.FileManager;

public class DatabaseManager {
    /* We can also expand this with methods like:
       beginTransaction(), commit(), rollback(), close(Statement stmt),
       close(ResultSet rs) to handle more JDBC resources and logging()
     */
    private static final String DB_URL;

    static {
        String dbFilePath = FileManager.getDatabaseFilePath();
        DB_URL = "jdbc:sqlite:" + dbFilePath;
    }

    public static Connection getDatabaseConnection() {
        // check for SQLite JDBC driver existence
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
    /* This below method is kept as a helper for future flexibility to close a database manually.
    * i.e. when not using try-catch with resources that close any resources automatically.*/
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("❌ Failed to close DB connection: " + e.getMessage());
            }
        }
    }

    /*
    // This class will handle connecting to SQLite app.database file.
    // Private static final String DB_URL = "jdbc:sqlite:accountsDB.db"; // stored in project root
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
