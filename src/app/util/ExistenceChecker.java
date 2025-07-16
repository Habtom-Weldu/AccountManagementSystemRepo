package app.util;

import database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExistenceChecker {
    // ### check phone number existence
    public static boolean checkIfPhoneExists(String phoneNumber) { // phone number is unique in our DB
        try (Connection conn = DatabaseManager.getDatabaseConnection()) {
            return isPhoneNumberExists(conn, phoneNumber);
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Treat error as duplicate for safety
        }
    }
    // ### Check phone number existence in a database before creating an account with the phone number input
    public static boolean isPhoneNumberExists(Connection conn, String phoneNum) throws SQLException {
        String query = "SELECT COUNT(*) FROM accountsTable WHERE phoneNumber = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, phoneNum);
            ResultSet rs = ps.executeQuery();
            //System.out.println(rs.next() && rs.getInt(1) > 0);
            return rs.next() && rs.getInt(1) > 0;
        }
    }
    public static boolean checkIfAccountNumberExist(Connection conn, String accountNumber) {
        String query = "SELECT 1 FROM accountsTable WHERE accNumber = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true if record exists
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ DB check failed", e);
        }
    }
}
