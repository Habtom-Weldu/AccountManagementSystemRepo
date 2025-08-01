package app;
import app.repository.AccountRepository;
import app.repository.DatabaseSetup;
import app.util.InputValidator;
import app.exceptions.GoBackToMainMenuException;
import app.services.AccountService;
import app.models.Account;
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
        DatabaseSetup.createAccountsTable(); // ✅ Ensure table is created
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
            System.out.println("7. Deposit");
            System.out.println("8. Withdraw money");
            System.out.println("9. Exit");

            System.out.print("Enter your choice: ");
            menuChoice = InputValidator.readIntInRange(1, 8, "Select a menu option (1–8): ");
            //sc.nextLine(); // clear buffer
            System.out.println("(You can type 'back' to get back to main Menu.)");
            switch (menuChoice) {
                case 1: // Case Create account
                    try {
                        // Call the validated input methods by passing Prompt message
                        String name = InputValidator.readValidName("Enter customer name: ");
                        //String email = InputValidator.readValidEmail("Enter Email: ");
                        String email = InputValidator.readValidEmail("Enter Email: ",
                                ExistenceChecker::checkIfEmailExists);
                        // Entry for phone number, here we are sending a function a parameter.
                        String phoneNo = InputValidator.readValidPhoneNumber(
                                "Enter phone number (international format): ", ExistenceChecker::checkIfPhoneExists);
                        String accountType = InputValidator.readValidAccountType("Enter Account Type " +
                                "(Savings/Checking/Business): ");
                        double initialBalance = InputValidator.readBalanceForAccountType("Enter initial balance ",
                                accountType);
                        // Create the account — all DB logic is inside accountService.createAccount(..) method
                        accountService.createAccount(name, initialBalance, email, phoneNo, accountType);
                    } catch (GoBackToMainMenuException e) {
                        System.out.println("↩️ Going back to main menu...");
                        continue; // loop again, to display the main menu
                    }
                    break;
                case 2:
                    try {
                        String accNumberToDelete = InputValidator.readValidAccNumber("Enter Account Number to delete: ");
                        boolean deleted = accountService.deleteAccount(accNumberToDelete);
                        System.out.println(deleted ? "✅ Account deleted." : "⚠️ Account not found.");
                    } catch (GoBackToMainMenuException e) {
                        System.out.println("↩️ Going back to main menu...");
                        continue; // loop again, to display the main menu
                    }
                    break;
                case 3: // Update
                    try {
                        if (accountService.updateAccountInteractive()) {
                            System.out.println("✅ Account updated successfully.");
                        } else {
                            System.out.println("⚠️ Account update failed.");
                        }
                    } catch (GoBackToMainMenuException e) {
                        System.out.println("↩️ Going back to main menu...");
                        continue; // loop again, to display the main menu
                    }
                    break;
                case 4:
                    try {
                        String accNumberToView = InputValidator.readValidAccNumber("Enter Account Number to view: ");
                        Account acc = accountService.getAccountByAccNum(accNumberToView);
                        if (acc != null) {
                            System.out.println("\n" + acc);
                        } else {
                            System.out.println("⚠️ Account not found.");
                        }
                    } catch (GoBackToMainMenuException e) {
                        System.out.println("↩️ Going back to main menu...");
                        continue; // loop again, to display the main menu
                    }
                    break;
                case 5:
                    accountService.displayAccounts();
                    break;
                case 6: // for saving
                    //accountService.saveAccount(newAcc);
                    break;
                case 7: // Deposit
                    break;
                case 8: //Withdraw money
                    break;
                case 9: // for exiting
                    //accountService.saveAccount();
                    System.out.println("👋 Exiting program...");
                    break;
                default:
                    System.out.println("⚠️ Invalid choice. Try again.");
            }
        } while (menuChoice != 7);
    }
}
