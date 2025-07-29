package services;
import exceptions.GoBackToMainMenuException;
import models.Account; // to use the class from 'models' package we created in this project
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import app.util.ExistenceChecker;

import database.*;
import app.util.InputValidator;

/* what will this do?
   - Keeps all account objects in memory (HashMap).
   - Loads/saves them from/to database/file.
   - Adds, deletes, updates and looks up accounts.
   - Cleanly separates logic from your main() class.
 */
public class AccountService {
    private static final Scanner sc = new Scanner(System.in);
    private final HashMap<String, Account> hmAccounts = new HashMap<>();
    private String filePath;

    /*public AccountService(String fp) {
        this.filePath = fp;
        loadAccounts(); // load accounts into the program
    } */
    public AccountService() {
        loadAccounts(); // load accounts into the program
    }

    // ########### Generate  Account number inorder to create an account
    private static final Random random = new Random();
    public static String generateUniqueAccountNumber(Connection conn) {
        String accountNumber;
        int maxAttempts = 100;
        for (int i = 0; i < maxAttempts; i++) { // Generate a random 10-digit number
            accountNumber = String.format("%010d", random.nextLong(1_000_000_000L));
            if (!ExistenceChecker.checkIfAccountNumberExist(conn, accountNumber)) { // Check if it already exists in DB
                return accountNumber;
            }
        }
        throw new RuntimeException("⚠️ Failed to generate unique account number after " +
                maxAttempts + " attempts.");
    }
    // ### Create and store a new account
    public boolean createAccount(String name, double balance, String email, String phoneNumber, String accountType) {
        try (Connection conn = DatabaseManager.getDatabaseConnection()) {
            String newAccNumber = generateUniqueAccountNumber(conn);
            Account newAccount = new Account(newAccNumber, name, balance, email, phoneNumber, accountType);
            // Insert to DB (call DAO or write logic here)
            database.AccountDBHelper.insertAccount(conn, newAccount);
            hmAccounts.put(newAccNumber, newAccount); // updating our hash map
            //System.out.println("✅ Account created successfully.");
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
    public Account getAccountByNumber(String accNumber) {
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
            if (e.getMessage().toLowerCase().contains("database is locked")) {
                System.out.println("⚠️ Database is locked. Please close other applications accessing it and try again.");
            } else {
                System.err.println("Error loading accounts: " + e.getMessage());
            }
        }
        return hmAccounts;
    }
    // Update Account
    public boolean updateAccount(Account account) {
        try (Connection conn = DatabaseManager.getDatabaseConnection()) {
            return AccountDBHelper.updateAccount(account, conn);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // ### Update Account Interactive code
    public boolean updateAccountInteractive() throws GoBackToMainMenuException {
        String accNumberToUpdate = InputValidator.readValidAccNumber("Enter account number to update: ");
        Account existingAcc = getAccountByNumber(accNumberToUpdate);
        if (existingAcc == null) {
            System.out.println("❌ Account not found.");
            return false;
        }
        System.out.println("Leave fields empty to keep current values.");
        // Account Type
        System.out.println("Account type:" + existingAcc.getAccountType() + " (not editable)");
        // Name
        existingAcc.setName(InputValidator.readValidNameOrDefault("Enter new name",
                existingAcc.getName()));
        // Email: Get email by passing prompt, current email and function checker
        String email = InputValidator.readValidEmailOrDefault(
                "Enter new Email: (" + existingAcc.getEmail() + "): ", existingAcc.getEmail(),
                ExistenceChecker::checkIfEmailExists);
        existingAcc.setEmail(email);
        // Phone
        String phone = InputValidator.readValidPhoneNumberOrDefault("Enter new phone number (" +
            existingAcc.getPhoneNumber() + "): ", existingAcc.getPhoneNumber(), ExistenceChecker::checkIfPhoneExists);
          existingAcc.setPhoneNumber(phone);
        // Balance
        existingAcc.setBalance(InputValidator.readBalanceOrDefault(existingAcc.getAccountType(),
                existingAcc.getBalance()));
        // Update Account
        return updateAccount(existingAcc);
    }

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
