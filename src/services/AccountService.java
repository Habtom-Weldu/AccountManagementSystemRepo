package services;
import models.Account; // to use the class from models package we created in this project
import java.io.*;
import java.util.HashMap;

/* what will this do?
   - Keeps all account objects in memory (HashMap).
   - Loads/saves them from/to file.
   - Adds, deletes, and looks up accounts.
   - Cleanly separates logic from your main() class.
 */
public class AccountService {
    private HashMap<String, Account> hmAccounts = new HashMap<>();
    private String filePath;

    public AccountService(String fp) {
        this.filePath = fp;
        loadAccounts();
    }
    // ### Create and store a new account
    public boolean createAccount(Account account) {
        if (hmAccounts.containsKey(account.getAccNumber())) {
            return false; // Duplicate account number
        }
        hmAccounts.put(account.getAccNumber(), account);
        return true;
    }
    // ### Get a single account by account number
    public Account getAccount(String accNumber) {
        return hmAccounts.get(accNumber);
    }

    // ### Get all accounts
    public HashMap<String, Account> getAllAccounts() {
        return hmAccounts;
    }
    // ### Save all accounts to file
    public void saveAccounts() {
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
            System.out.println("⚠️ Error saving accounts: " + e.getMessage());
        }
    }
    // Load accounts from file into memory
    @SuppressWarnings("unchecked")
    private void loadAccounts() {
        File file = new File(filePath);
        if (!file.exists()) {
            return; // No saved accounts yet
        }

        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            int count = ois.readInt();
            for (int i = 0; i < count; i++) {
                Account acc = (Account) ois.readObject();
                hmAccounts.put(acc.getAccNumber(), acc);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("⚠️ Error loading accounts: " + e.getMessage());
        }
    }
}
