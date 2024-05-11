package org.example.atomicity;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankService {
    public static void main(String [] args) throws InterruptedException {
        boolean useThreadSafe = true;
        BankAccount bankAccount = createBankAccount(useThreadSafe);

        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        try {

            CountDownLatch countDownLatch = new CountDownLatch(10);

            for (int i = 0; i < 10; i++) {
                threadPool.submit(() -> {
                    for (int j = 0; j < 1000; j++) {
                        bankAccount.add(10);
                        bankAccount.subtract(10);
                    }
                    countDownLatch.countDown();
                });
            }

            System.out.println("Waiting for threads to finish");
            countDownLatch.await();
            System.out.println("current balance = " + bankAccount.getBalance());
        }
        finally {
            threadPool.shutdown();
        }
    }

    public static BankAccount createBankAccount(boolean useThreadSafe){
        return useThreadSafe ? new ThreadSafeBankAccount() : new NonThreadsSafeBankAccount();
    }
}
