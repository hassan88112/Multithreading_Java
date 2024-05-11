package org.example.atomicity;

public interface BankAccount {
    void add(int amount);
    void subtract(int amount);
    int getBalance();
}
