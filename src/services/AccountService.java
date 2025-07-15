package services;
import models.Account; // to use the class from 'models' package we created in this project
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;
import database.DatabaseManager;

/* what will this do?
   - Keeps all account objects in memory (HashMap).
   - Loads/saves them from/to file.
   - Adds, deletes, and looks up accounts.
   - Cleanly separates logic from your main() class.
 */
public class AccountService {
    private HashMap<String, Account> hmAccounts = new HashMap<>();
    private String filePath;

    /*public AccountService(String fp) {
        this.filePath = fp;
        loadAccounts(); // load accounts into the program
    } */
    public AccountService() {
        loadAccounts(); // load accounts into the program
    }

    // ########### Generate  Account number in order to create an account
    private static final Random random = new Random();
    public static String generateUniqueAccountNumber(Connection conn) {
        String accountNumber;
        int maxAttempts = 100;
        for (int i = 0; i < maxAttempts; i++) { // Generate a random 10-digit number
            accountNumber = String.format("%010d", random.nextLong(1_000_000_000L));
            if (!accountNumberExistsInDB(conn, accountNumber)) { // Check if it already exists in DB
                return accountNumber;
            }
        }
        throw new RuntimeException("⚠️ Failed to generate unique account number after " +
                maxAttempts + " attempts.");
    }
    private static boolean accountNumberExistsInDB(Connection conn, String accountNumber) {
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
    // ### Check phone number existence before creating an account with the phone number input
    public boolean isPhoneNumberExists(Connection conn, String phoneNum) throws SQLException {
        String query = "SELECT COUNT(*) FROM accountsTable WHERE phoneNumber = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, phoneNum);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
    // ### Create and store a new account
    public boolean createAccount(String name, double balance, String email, String phoneNumber, String accountType) {
        try (Connection conn = DatabaseManager.getDatabaseConnection()) {
            String newAccNumber = generateUniqueAccountNumber(conn);
            Account newAccount = new Account(newAccNumber, name, balance, email, phoneNumber, accountType);
            // Insert to DB (call DAO or write logic here)
            database.AccountDBHelper.insertAccount(conn, newAccount);
            hmAccounts.put(newAccNumber, newAccount); // updating our hash map
            System.out.println("✅ Account created successfully.");
        } catch (SQLException e) {
            System.out.println("❌ Failed to create account: " + e.getMessage());
        }
        return true;
    }
    // ########### Delete account by account number
    public boolean deleteAccount(String accNumber) {
        database.AccountDBHelper.deleteAccountFromDB(accNumber); // delete from database table as well
        return hmAccounts.remove(accNumber) != null;
    }
    // ########### Get a single account by account number
    public Account getAccount(String accNumber) {
        //loadAccounts();
        /* we do not need loadAccounts() method call, coz when we call
        accountService.getAccount(viewAccNum); we already created an object of AccountService class,
        and this class is already calling loadAccounts() in its constructor */
        return hmAccounts.get(accNumber);
    }

    // ########### Get all accounts
    public HashMap<String, Account> getAllAccounts() {
        //loadAccounts();
        return hmAccounts;
    }
    // ########### Load Accounts into HashMap
    public HashMap<String, Account> loadAccounts() {
        HashMap<String, Account> accounts = new HashMap<>();
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
                    hmAccounts.put(acc.getAccNumber(), acc);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading accounts: " + e.getMessage());
        }
        return hmAccounts;

    }
    /* public boolean saveAccount(Account acc) {
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
                insertStmt.executeUpdate();
            }
            System.out.println("✅ Account saved.");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error saving account: " + e.getMessage());
            return false;
        }
    } */

    // ### Save all accounts to file
    /*public void saveAccounts() {
        try (FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeInt(hmAccounts.size()); // we want to save the number of accounts at the first
            // position of the file.
            for (Account a : hmAccounts.values()) {
                oos.writeObject(a);
            }
            oos.flush();
            System.out.println("✅ Accounts saved to file.");

        } catch (IOException e) {
            System.out.println("❌ Failed to save accounts");
            e.printStackTrace();
        }
    } */
    // Load accounts from file into memory
    /*@SuppressWarnings("unchecked")
    private void loadAccounts() {
        File file = new File(filePath);
        if (!file.exists()) {
            //System.out.println("No accounts file found or file is empty. Starting with an empty list.");
            System.out.println("No accounts file found ");
            return; // No saved accounts yet
        } else if (file.length() == 0) {
            System.out.println("File is empty");
            return;
        }
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            int count = ois.readInt();
            for (int i = 0; i < count; i++) {
                Account acc = (Account) ois.readObject();
                hmAccounts.put(acc.getAccNumber(), acc);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("⚠️ No data file found at " + filePath + ". Starting with an empty list.");
        }
        catch (Exception e) {
            System.out.println("❌ Error loading accounts from file:");
            e.printStackTrace(); // 🔍 Full error details
        }
    }*/
}
