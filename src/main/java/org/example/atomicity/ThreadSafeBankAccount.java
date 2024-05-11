package org.example.atomicity;

import com.google.errorprone.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
public class ThreadSafeBankAccount implements BankAccount {
    private AtomicInteger balance;

    public ThreadSafeBankAccount(int initialBalance) {
        balance = new AtomicInteger(initialBalance);
    }

    public ThreadSafeBankAccount() {
        balance = new AtomicInteger(0);
    }

    public void add(int amount){
        this.balance.addAndGet(amount);
    }

    public void subtract(int amount){
        this.balance.addAndGet(-amount);
    }

    public int getBalance() {
        return balance.get();
    }
}
