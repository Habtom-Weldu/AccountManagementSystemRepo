package app.services;
import app.repository.AccountRepository;
import app.exceptions.GoBackToMainMenuException;
import app.models.Account; // to use the class from 'app.models' package we created in this project
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import app.repository.DatabaseManager;
import app.util.ExistenceChecker;

import app.util.InputValidator;

/* what will this do?
   - Keeps all account objects in memory (HashMap).
   - Loads/saves them from/to app.database/file.
   - Adds, deletes, updates and looks up accounts.
   - Cleanly separates logic from your main() class.
 */
public class AccountService {
    private static final Scanner sc = new Scanner(System.in);
    private final HashMap<String, Account> hmAccounts = new HashMap<>();
    private String filePath;
    public AccountService() {
        loadAccountsToMemory(); // load accounts into the program
    }

    // Create Instance of AccountRepository
    /* We can call instance methods like 'getAllAccounts' in AccountRepository,
       by creating instance of the class having instance method in it*/
    private AccountRepository AccRepoInstance = new AccountRepository();
    public AccountService(AccountRepository repo) { // constructor that accepts AccountRepository reference
        this.AccRepoInstance = repo; // now we can use AccRepoInstance to call instance methods in AccountRepository.java
    }
    // ########### Load Accounts into HashMap/To Memory
    public void loadAccountsToMemory() {
        List<Account> accsList = AccRepoInstance.getAllAccounts();
        hmAccounts.clear();  // clear old data before loading new
        for (Account acc : accsList) {
            hmAccounts.put(acc.getAccNumber(), acc);
        }
    }
    public void displayAccounts() {
        if (hmAccounts.isEmpty()) {
            System.out.println("⚠️ No accounts available.");
        } else {
            for (Account acc : hmAccounts.values()) {
                System.out.println("\n" + acc);
            }
        }
    }
    // ########### Generate  Account number inorder to create an account
    private static final Random random = new Random();
    public static String generateUniqueAccountNumber(Connection conn) {
        /* this will generate account number which is not
           already used (do not exist in a database for another customer's account) used */
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
            String newAccNumber = generateUniqueAccountNumber(conn); // this will generate account number which is not
            // already used (do not exist in a database for another customer's account) used
            Account newAccount = new Account(newAccNumber, name, balance, email, phoneNumber, accountType);
            // Insert to DB (call DAO or write logic here)
            AccountRepository.insertAccount(conn, newAccount);
            hmAccounts.put(newAccNumber, newAccount); // updating our hash map
            //System.out.println("✅ Account created successfully.");
        } catch (SQLException e) {
            System.out.println("❌ Failed to create account: " + e.getMessage());
        }
        return true;
    }
    // ########### Delete account by account number
    public boolean deleteAccount(String accNumber) {
        Account acc = getAccountByAccNum(accNumber);
        if (acc != null) {
            AccountRepository.deleteAccountFromDB(accNumber); // delete from app.database table as well
            return hmAccounts.remove(accNumber) != null;
        } else {
            System.out.println("⚠️ Account not found.");
            return false;
        }
    }
    // ########### Get a single account by account number
    public Account getAccountByAccNum(String accNumber) {
        //loadAccountsToMemory();
        /* we do not need loadAccountsToMemory() method call, coz when we call
        accountService.getAccount(viewAccNum); we already created an object of AccountService class,
        and this class is already calling loadAccounts() in its constructor */
        return hmAccounts.get(accNumber);
    }

    // ########### Get all accounts
    /*public HashMap<String, Account> getAllAccounts() {
        //loadAccounts();
        return hmAccounts;
    } */

    // Update Account
    public boolean updateAccount(Account account) {
        try (Connection conn = DatabaseManager.getDatabaseConnection()) {
            boolean updateSuccess = AccountRepository.updateAccountInDB(account,conn);
            if (updateSuccess) {
                hmAccounts.put(account.getAccNumber(), account); // To update in-memory HashMap as well
            }
            return updateSuccess;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // ### Update Account Interactive code
    public boolean updateAccountInteractive() throws GoBackToMainMenuException {
        String accNumberToUpdate = InputValidator.readValidAccNumber("Enter account number to update: ");
        Account existingAcc = getAccountByAccNum(accNumberToUpdate);
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

    // ############ Deposit Amount balance
     public boolean deposit(String accountNum, double amountToDeposit) {
             try (Connection conn = DatabaseManager.getDatabaseConnection()) {
                 Account acc = getAccountByAccNum(accountNum);
                 if (acc == null) {
                     System.out.println("❌ Account not found.");
                     return false;
                 }
                 double currentBalance = acc.getBalance();
                 if (amountToDeposit <= 0) {
                     System.out.println("❌ Deposit amount must be greater than zero.");
                     return false;
                 }
                 if (amountToDeposit < 1.0) {
                     System.out.println("❌ Minimum deposit is $1.00.");
                     return false;
                 }
                 if (amountToDeposit > 10000.0) {
                     System.out.println("❌ Maximum allowed per deposit is $10,000.00");
                     return false;
                 }
                 // Update balance
                 double newBalance = currentBalance + amountToDeposit;
                 acc.setBalance(newBalance);
                 // Update balance in database
                 boolean depositSuccess = AccountRepository.updateAccountInDB(acc, conn);
                 if (depositSuccess) {
                     // Update in-memory HashMap (not strictly necessary if reference is same)
                     hmAccounts.put(accountNum, acc);
                     System.out.println("✅ Deposit successful. New balance: $" + newBalance);
                     return true;
                 } else {
                     System.out.println("❌ Failed to update account in database.");
                     return false;
                 }
             } catch (SQLException e) {
                 e.printStackTrace();
                 return false;
             }
    }
    // Withdraw Amount balance
    public boolean withdraw(String accountNum, double amountToWithdraw) {
        try (Connection conn = DatabaseManager.getDatabaseConnection()) {
            Account account = getAccountByAccNum(accountNum);
            if (account == null) {
                System.out.println("❌ Account not found.");
                return false;
            }
            double currentBalance = account.getBalance();
            double minimumBalance = 10.0;

            if (amountToWithdraw < 1.0) {
                System.out.println("❌ Minimum withdraw is $1.00.");
                return false;
            }
            if (amountToWithdraw > currentBalance) {
                System.out.println("❌ Insufficient balance.");
                return false;
            }
            if((currentBalance - amountToWithdraw) < minimumBalance){
                System.out.println("❌ Withdrawal would drop balance below the minimum allowed of $" + minimumBalance);
                return false;
            }
            if (amountToWithdraw > 10000.0) {
                System.out.println("❌ Maximum allowed per withdraw is $10,000.00");
                return false;
            }
            // Update balance
            double newBalance = currentBalance - amountToWithdraw;
            account.setBalance(newBalance);
            // Update balance in database
            boolean withdrawSuccess = AccountRepository.updateAccountInDB(account, conn);
            if (withdrawSuccess) {
                // Update in-memory HashMap (not strictly necessary if reference is same)
                hmAccounts.put(accountNum, account);
                System.out.println("✅ Withdrawal successful. New balance: $" + newBalance);
                return true;
            } else {
                System.out.println("❌ Failed to update account in database.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
