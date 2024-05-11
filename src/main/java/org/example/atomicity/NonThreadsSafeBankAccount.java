package org.example.atomicity;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class NonThreadsSafeBankAccount implements BankAccount {
    private int balance;

    public NonThreadsSafeBankAccount() {
        balance = 0;
    }

    public void add(int amount){
        this.balance += amount;
    }

    public void subtract(int amount){
        this.balance -= amount;
    }

    public int getBalance() {
        return balance;
    }
}
