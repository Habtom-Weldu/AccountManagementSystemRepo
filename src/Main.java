
import services.AccountService;
import models.Account;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;
public class Main {
    /* This class:
     - Uses the AccountService for all logic
     - Loads and saves accounts using FileManager
     - Handles menu options cleanly
    */

    public static void main(String[] args) {
        //2. we need to define input from the keyboard inside this main method
        Scanner sc = new Scanner(System.in);
        String filePath = FileManager.getFilePath();
        AccountService accountService = new AccountService(filePath);

        int choice;
        do {
            System.out.println("\n--- Account Management Menu ---");
            System.out.println("1. Create Account");
            System.out.println("2. Delete Account");
            System.out.println("3. View Account");
            System.out.println("4. View All Accounts");
            System.out.println("5. Save Accounts");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    System.out.print("Enter Account Number: ");
                    String accNo = sc.nextLine();

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Balance: ");
                    double balance = sc.nextDouble();
                    sc.nextLine(); // clear buffer

                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();

                    System.out.print("Enter Phone Number: ");
                    String phoneNumber = sc.nextLine();

                    System.out.print("Enter Account Type (Savings/Checking): ");
                    String accountType = sc.nextLine();

                    Account newAcc = new Account(accNo, name, balance,
                    email, phoneNumber, accountType);
                    newAcc.setEmail(email);
                    newAcc.setPhoneNumber(phoneNumber);
                    newAcc.setAccountType(accountType);

                    boolean created = accountService.createAccount(newAcc);
                    if (created) {
                        System.out.println("✅ Account created successfully.");
                    } else {
                        System.out.println("⚠️ Account with this number already exists.");
                    }
                    break;

                case 2:
                    System.out.print("Enter Account Number to delete: ");
                    String delAcc = sc.nextLine();
                    boolean deleted = accountService.deleteAccount(delAcc);
                    System.out.println(deleted ? "✅ Account deleted." : "⚠️ Account not found.");
                    break;

                case 3:
                    System.out.print("Enter Account Number to view: ");
                    String viewAcc = sc.nextLine();
                    Account acc = accountService.getAccount(viewAcc);
                    if (acc != null) {
                        System.out.println("\n" + acc);
                    } else {
                        System.out.println("⚠️ Account not found.");
                    }
                    break;
                case 4:
                    HashMap<String, Account> allAccounts = accountService.getAllAccounts();
                    if (allAccounts.isEmpty()) {
                        System.out.println("⚠️ No accounts available.");
                    } else {
                        for (Account a : allAccounts.values()) {
                            System.out.println("\n" + a);
                        }
                    }
                    break;
                case 5:
                    accountService.saveAccounts();
                    break;
                case 6:
                    accountService.saveAccounts();
                    System.out.println("👋 Exiting program...");
                    break;
                default:
                    System.out.println("⚠️ Invalid choice. Try again.");
            }
        } while (choice != 6);
    }
}
