package database;
import models.Account;
import java.sql.*;
import database.DatabaseManager;
public class AccountDBHelper {
    public static boolean insertAccount(Connection conn, Account acc) {
            // Account exists in DB, is already checked while generating account number in AccountService.java
                String insertSql = """
                INSERT INTO accountsTable (accNumber, name, balance, email, phoneNumber, accountType)
                VALUES (?, ?, ?, ?, ?, ?);""";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSql)){
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setString(1, acc.getAccNumber());
                insertStmt.setString(2, acc.getName());
                insertStmt.setDouble(3, acc.getBalance());
                insertStmt.setString(4, acc.getEmail());
                insertStmt.setString(5, acc.getPhoneNumber());
                insertStmt.setString(6, acc.getAccountType());
                // the isActive and dateCreate are defaulted in the database
                insertStmt.executeUpdate();
                //System.out.println("✅ Account created and successfully saved. Account No: " + acc.getAccNumber());
                return true;
        } catch (SQLException e) {
            //System.err.println("❌ Error saving account: " + e.getMessage());
            // SQLite constraint violation code is "SQLITE_CONSTRAINT" (error code 19)
            if (e.getErrorCode() == 19 || e.getMessage().contains("UNIQUE constraint failed")) {
                System.out.println("⚠️ Duplicate account number detected. Please try again.");
            } else {
                System.out.println("❌ Database error: " + e.getMessage());
            }
            return false;
        }
    }

    public static boolean deleteAccountFromDB(String accNumber) {
        String sql = "DELETE FROM accountsTable WHERE accNumber = ?";

        try (Connection conn = DatabaseManager.getDatabaseConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, accNumber);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Delete failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateAccountInDB(Account acc) {
        String sql = """
            UPDATE accountsTable SET name = ?, balance = ?, email = ?,
                phoneNumber = ?, accountType = ?, isActive = ?, dateCreated = ? WHERE accNumber = ?;""";

        try (Connection conn = DatabaseManager.getDatabaseConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, acc.getName());
            pstmt.setDouble(2, acc.getBalance());
            pstmt.setString(3, acc.getEmail());
            pstmt.setString(4, acc.getPhoneNumber());
            pstmt.setString(5, acc.getAccountType());
            pstmt.setString(6, acc.getAccNumber());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Update failed: " + e.getMessage());
            return false;
        }
    }
}
