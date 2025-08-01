package app.repository;
import app.models.Account;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {
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
                // the isActive and dateCreate are defaulted in the app.database
                insertStmt.executeUpdate();
                System.out.println("✅ Account created and successfully saved. Account No: " + acc.getAccNumber());
                return true;
        } catch (SQLException e) {
            // SQLite constraint violation code is "SQLITE_CONSTRAINT" (error code 19)
            if (e.getMessage().contains("UNIQUE constraint failed: accountsTable.accNumber")) {
                System.out.println("⚠️ Duplicate account number detected. Please try again.");
            } else if (e.getMessage().contains("UNIQUE constraint failed: accountsTable.phoneNumber")) {
                System.out.println("⚠️ Duplicate phone number detected. Please try again.");
            } else if (e.getMessage().contains("UNIQUE constraint failed: accountsTable.email")) {
                System.out.println("⚠️ Duplicate email detected. Please try again.");
            }  else if (e.getMessage().toLowerCase().contains("app.database is locked")) {
                System.out.println("⚠️ Database is locked. Please close other applications accessing it and try again.");
            } else {
                System.out.println("❌ Database error: " + e.getMessage());
            }
            return false;
        }
    }
    public static boolean updateAccount(Account updatedAccount, Connection conn) throws SQLException {
        String sql = "UPDATE accountsTable SET name = ?, balance = ?, email = ?, phoneNumber = ? WHERE accNumber = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, updatedAccount.getName());
            ps.setDouble(2, updatedAccount.getBalance());
            ps.setString(3, updatedAccount.getEmail());
            ps.setString(4, updatedAccount.getPhoneNumber());
            ps.setString(5, updatedAccount.getAccNumber());
            return ps.executeUpdate() > 0;
        } catch (Exception e){
            // SQLite constraint violation code is "SQLITE_CONSTRAINT" (error code 19)
            if (e.getMessage().contains("UNIQUE constraint failed: accountsTable.phoneNumber")) {
                System.out.println("⚠️ Duplicate phone number detected. Please try again.");
            }else if (e.getMessage().contains("UNIQUE constraint failed: accountsTable.email")) {
                System.out.println("⚠️ Duplicate email detected. Please try again.");
            } else if (e.getMessage().contains("UNIQUE constraint failed: accountsTable.email")) {
                System.out.println("⚠️ Duplicate email detected. Please try again.");
            } else if (e.getMessage().toLowerCase().contains("app.database is locked")) {
                System.out.println("⚠️ Database is locked. Please close other applications accessing it and try again.");
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
             if (e.getMessage().toLowerCase().contains("app.database is locked")) {
                System.out.println("⚠️ Database is locked. Please close other applications accessing it and try again.");
            } else {
                 System.err.println("❌ Delete failed: " + e.getMessage());
             }
            return false;
        }
    }
    public List<Account> getAllAccounts() {
        List<Account> accountsList = new ArrayList<>();
        String sql = "SELECT * FROM accountsTable;";
        try (Connection conn = DatabaseManager.getDatabaseConnection()) {
            assert conn != null; // this requires to Enable Assertions in IntelliJ
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                while (rs.next()) {
                    Account acc = new Account(
                            rs.getString("accNumber"),
                            rs.getString("name"),
                            rs.getDouble("balance"),
                            rs.getString("email"),
                            rs.getString("phoneNumber"),
                            rs.getString("accountType"),
                            rs.getInt("isActive") == 1,
                            LocalDateTime.parse(rs.getString("dateCreated"), myDateFormatter)
                    );
                    accountsList.add(acc);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching accounts: " + e.getMessage());
        }
        return accountsList;
    }
}
