package app.models;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Account implements Serializable {
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
    LocalDateTime dateCreated;
    public Account(){}
    public Account(String accNumber, String name, double balance, String email, String phoneNumber,
                   String accountType) {
        this.accNumber = accNumber;
        this.name = name;
        this.balance = balance;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.accountType = accountType;
        this.isActive = true;
        this.dateCreated = LocalDateTime.now();
    }
    public Account(String accNumber, String name, double balance, String email, String phoneNumber,
                   String accountType, boolean isActive, LocalDateTime dateCreated) {
        this.accNumber = accNumber;
        this.name = name;
        this.balance = balance;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.accountType = accountType;
        this.isActive = isActive;
        this.dateCreated = dateCreated;
    }
    // Getters
    public String getAccNumber() {
        return accNumber;
    }
    public String getName() {
        return name;
    }
    public double getBalance() {
        return balance;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getAccountType() {
        return accountType;
    }
    public boolean getActive() {
        return isActive;
    }
    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    public void setActive(boolean isActive) {
        isActive = isActive;
    }

    public String toString(){
        return "Account No: " + accNumber +
                "\nName: " + name +
                "\nBalance: " + balance +
                "\nEmail: " + email +
                "\nPhone: " + phoneNumber +
                "\nType: " + accountType +
                "\nActive: " + isActive +
                "\nCreated: " + dateCreated;
    }
}
