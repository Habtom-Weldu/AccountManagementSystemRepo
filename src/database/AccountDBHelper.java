package database;
import models.Account;
import java.sql.*;
import database.DatabaseManager;
public class AccountDBHelper {
    public static boolean insertAccount(Account acc) {
        try (Connection conn = DatabaseManager.getConnection()) {
            // Check if an account exists
            String checkSql = "SELECT COUNT(*) FROM accountsTable WHERE accNumber = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, acc.getAccNumber());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Account exist");
            }
            else
            {
                // Insert
                String insertSql = """
                INSERT INTO accountsTable (accNumber, name, balance, email, phoneNumber, accountType)
                VALUES (?, ?, ?, ?, ?, ?);""";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setString(1, acc.getAccNumber());
                insertStmt.setString(2, acc.getName());
                insertStmt.setDouble(3, acc.getBalance());
                insertStmt.setString(4, acc.getEmail());
                insertStmt.setString(5, acc.getPhoneNumber());
                insertStmt.setString(6, acc.getAccountType());
                // the isActive and dateCreate are defaulted in the database
                insertStmt.executeUpdate();
            }
            System.out.println("✅ Account saved.");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error saving account: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteAccountFromDB(String accNumber) {
        String sql = "DELETE FROM accountsTable WHERE accNumber = ?";

        try (Connection conn = DatabaseManager.getConnection();
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

        try (Connection conn = DatabaseManager.getConnection();
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
