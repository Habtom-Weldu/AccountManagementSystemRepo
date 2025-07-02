package models;
import java.io.Serializable;
import java.time.LocalDate;
class Account implements Serializable {
    // Identity info
    String accNumber;
    String name;
    // Contact info
    String email;
    String phoneNumber;
    // Account info
    String accountType;
    double balance;
    boolean isActive;
    LocalDate createdDate;
    public Account(){}
    public Account(String accNumber, String name, double balance,
                   String email, String phoneNumber, String accountType) {
        this.accNumber = accNumber;
        this.name = name;
        this.balance = balance;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.accountType = accountType;
        this.isActive = true;
        this.createdDate = LocalDate.now();
    }
    public String toString(){
        return "Account No: " + accNumber +
                "\nName: " + name +
                "\nBalance: " + balance +
                "\nEmail: " + email +
                "\nPhone: " + phoneNumber +
                "\nType: " + accountType +
                "\nActive: " + isActive +
                "\nCreated: " + createdDate;
    }
}
