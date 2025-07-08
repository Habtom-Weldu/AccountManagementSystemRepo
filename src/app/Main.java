package app;
import app.util.InputValidator;
import services.AccountService;
import models.Account;
import java.util.HashMap;
import java.util.Scanner;
public class Main {
    /* This class:
     - Uses the AccountService for all logic
     - Loads and saves accounts using app.util.FileManager
     - Handles menu options cleanly
    */

    public static void main(String[] args) {
        database.DatabaseSetup.createAccountsTable(); // ✅ Ensure table is created
        //2. we need to define input from the keyboard inside this main method
        Scanner sc = new Scanner(System.in);
        //String filePath = FileManager.getDatabaseFilePath();
        //AccountService accountService = new AccountService(filePath);
        AccountService accountService = new AccountService();
        int menuChoice;
        do {
            System.out.println("\n===== Account Management Menu =====");
            System.out.println("1. Create Account");
            System.out.println("2. Delete Account");
            System.out.println("3. Update Account");
            System.out.println("4. View Account");
            System.out.println("5. View All Accounts");
            System.out.println("6. Save Accounts");
            System.out.println("7. Exit");

            System.out.print("Enter your choice: ");
            menuChoice = InputValidator.readIntInRange(1, 8, "Select a menu option (1–8): ");
            //sc.nextLine(); // clear buffer

            switch (menuChoice) {
                case 1:
                    System.out.print("Enter Account Number: ");
                    String accNo = sc.nextLine();

                    // Call the validated name input method by passing Prompt message
                    String name = InputValidator.readValidName("Enter customer name: ");

                    System.out.print("Enter Balance: ");
                    double balance = sc.nextDouble();
                    sc.nextLine(); // clear buffer

                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();

                    System.out.print("Enter Phone Number: ");
                    String phoneNumber = sc.nextLine();

                    System.out.print("Enter Account Type (Savings/Checking): ");
                    String accountType = sc.nextLine();

                    Account newAcc = new Account(accNo, name, balance, email, phoneNumber,
                            accountType);

                    //boolean created = accountService.createAccount(newAcc); // 🔁 This should handle DB save too
                    accountService.createAccount(newAcc); // 🔁 This should handle DB save too
                    /*if (created) {
                        //System.out.println("✅ Account created successfully.");
                    } else {
                        System.out.println("⚠️ Account with this number already exists.");
                    } */
                    break;

                case 2:
                    System.out.print("Enter Account Number to delete: ");
                    String delAccNum = sc.nextLine();
                    boolean deleted = accountService.deleteAccount(delAccNum);
                    System.out.println(deleted ? "✅ Account deleted." : "⚠️ Account not found.");
                    break;

                case 3:
                    /*System.out.print("Enter Account Number to Update: ");
                    String updateAccNum = sc.nextLine();
                    boolean updated = accountService.updateAccount()(updateAccNum);
                    System.out.println(updated ? "✅ Account Updated." : "⚠️ Account not found."); */
                    break;
                case 4:
                    System.out.print("Enter Account Number to view: ");
                    String viewAccNum = sc.nextLine();
                    Account acc = accountService.getAccount(viewAccNum);
                    if (acc != null) {
                        System.out.println("\n" + acc);
                    } else {
                        System.out.println("⚠️ Account not found.");
                    }
                    break;
                case 5:
                    HashMap<String, Account> allAccounts = accountService.getAllAccounts();
                    if (allAccounts.isEmpty()) {
                        System.out.println("⚠️ No accounts available.");
                    } else {
                        for (Account a : allAccounts.values()) {
                            System.out.println("\n" + a);
                        }
                    }
                    break;
                case 6:
                    //accountService.saveAccount(newAcc);
                    break;
                case 7:
                    //accountService.saveAccount();
                    System.out.println("👋 Exiting program...");
                    break;
                default:
                    System.out.println("⚠️ Invalid choice. Try again.");
            }
        } while (menuChoice != 7);
    }
}
