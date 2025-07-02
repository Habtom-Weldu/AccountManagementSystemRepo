package models;

import java.io.Serializable;
class Account implements Serializable {
    String AccNumber;
    String Name;
    double balance;
    Account(){}
    Account(String acn, String n, double b){
        AccNumber = acn;
        Name = n;
        balance = b;
    }
    public String toString(){
        return "Account No: " + AccNumber + " Name: " + Name + " Balance: " + balance;
    }
}
