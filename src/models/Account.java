package models;
import java.io.Serializable;
import java.time.LocalDate;
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
    public boolean isActive() {
        return isActive;
    }
    public LocalDate getCreatedDate() {
        return createdDate;
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
    public void setActive(boolean active) {
        isActive = active;
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
