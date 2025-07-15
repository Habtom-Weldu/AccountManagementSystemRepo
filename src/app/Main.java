package app;
import app.util.InputValidator;
import database.DatabaseManager;
import services.AccountService;
import models.Account;

import java.sql.Connection;
import java.sql.SQLException;
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
                case 1:
                    // Call the validated input methods by passing Prompt message
                    String name = InputValidator.readValidName("Enter customer name: ");
                    String email = InputValidator.readValidEmail("Enter Email: ");

                    //System.out.print("Enter Phone Number: ");
                    //String phoneNumber = sc.nextLine();
                    String phoneNo = InputValidator.readValidPhoneNumber(
                            "Enter phone number (international format): ",
                            Main::checkIfPhoneExists
                    );
                    String accountType = InputValidator.readValidAccountType("Enter Account Type " +
                            "(Savings/Checking/Business): ");
                    double balance = InputValidator.readBalanceForAccountType("Enter initial balance ",
                            accountType);
                    // Create the account — all DB logic is inside accountService.createAccount(..) method
                    accountService.createAccount(name, balance, email, phoneNo, accountType);
                    break;
                case 2:
                    System.out.print("Enter Account Number to delete: ");
                    String delAccNum = sc.nextLine();
                    boolean deleted = accountService.deleteAccount(delAccNum);
                    System.out.println(deleted ? "✅ Account deleted." : "⚠️ Account not found.");
                    break;

                case 3: // Update
                    /*System.out.print("Enter Account Number to Update: ");
                    String updateAccNum = sc.nextLine();
                    boolean updated = accountService.updateAccount()(updateAccNum);
                    System.out.println(updated ? "✅ Account Updated.":"⚠️ Account not found."); */
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

    // ### check phone number existance
    private static AccountService accountService = new AccountService();
    private static boolean checkIfPhoneExists(String phoneNumber) {
        try (Connection conn = DatabaseManager.getDatabaseConnection()) {
            return accountService.isPhoneNumberExists(conn, phoneNumber);
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Treat error as duplicate for safety
        }
    }
}
