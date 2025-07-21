package database;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {
// This will create the account table if it does not exist yet.
    public static void createAccountsTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS accountsTable (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                accNumber TEXT UNIQUE NOT NULL,
                name TEXT NOT NULL,
                balance REAL NOT NULL DEFAULT 0.0,
                email TEXT NOT NULL UNIQUE,
                phoneNumber TEXT NOT NULL UNIQUE,
                accountType TEXT,
                isActive INTEGER DEFAULT 1,
                dateCreated TEXT DEFAULT CURRENT_TIMESTAMP
            );""";
        try (Connection conn = DatabaseManager.getDatabaseConnection()) {
            if (conn == null) {
                System.err.println("❌ Connection is null. Table creation aborted.");
                return;
            }

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                //System.out.println("✅ Table created or already exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // shows detailed reason
        }
    }
}

