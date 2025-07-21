package app;
import app.util.InputValidator;
import services.AccountService;
import models.Account;
import app.util.ExistenceChecker;

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
                case 1: // Case Create account
                    // Call the validated input methods by passing Prompt message
                    String name = InputValidator.readValidName("Enter customer name: ");
                    //String email = InputValidator.readValidEmail("Enter Email: ");
                    String email = InputValidator.readValidEmail(
                            "Enter Email: ",
                            ExistenceChecker::checkIfEmailExists
                    );
                    // Entry for phone number, here we are sending a function a parameter.
                    String phoneNo = InputValidator.readValidPhoneNumber(
                            "Enter phone number (international format): ",
                            ExistenceChecker::checkIfPhoneExists
                    );
                    String accountType = InputValidator.readValidAccountType("Enter Account Type " +
                            "(Savings/Checking/Business): ");
                    double initialBalance = InputValidator.readBalanceForAccountType("Enter initial balance ",
                            accountType);
                    // Create the account — all DB logic is inside accountService.createAccount(..) method
                    accountService.createAccount(name, initialBalance, email, phoneNo, accountType);
                    break;
                case 2:
                    String accNumberToDelete = InputValidator.readValidAccNumber("Enter Account Number to delete: ");
                    boolean deleted = accountService.deleteAccount(accNumberToDelete);
                    System.out.println(deleted ? "✅ Account deleted." : "⚠️ Account not found.");
                    break;

                case 3: // Update
                    if (accountService.updateAccountInteractive()) {
                        System.out.println("✅ Account updated successfully.");
                    } else {
                        System.out.println("⚠️ Account update failed.");
                    }
                    break;
                case 4:
                    String accNumberToView = InputValidator.readValidAccNumber("Enter Account Number to view: ");
                    Account acc = accountService.getAccountByNumber(accNumberToView);
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
