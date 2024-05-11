package org.example.deadlock;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.example.atomicity.ThreadSafeBankAccount;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeadlockDemo {
    public static void main(String [] args) throws InterruptedException {
        ThreadSafeBankAccount sourceBankAccount = new ThreadSafeBankAccount(100);
        ThreadSafeBankAccount destinationBankAccount = new ThreadSafeBankAccount(0);

        // transfer 10 from sourceBankAccount to destinationBankAccount
        // at the end, sourceBankAccount balance should have 0 and destinationBankAccount should have 100

        transferFromTo(sourceBankAccount, destinationBankAccount);

        System.out.println(String.format("sourceBankAccount balance = %d, destinationBankAccount balance = %d", sourceBankAccount.getBalance(), destinationBankAccount.getBalance()));
    }

    private static void transferFromTo(ThreadSafeBankAccount source, ThreadSafeBankAccount destination) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);

        ExecutorService threadPool = Executors.newFixedThreadPool(10,
                new ThreadFactoryBuilder().setNameFormat("TransferThread").build());
        try {
            // transfer 10 from source bankAccount1 to destination bankAccount 5 times
            for (int i = 0; i < 5; i++) {
                threadPool.submit(new TransferRunnable(countDownLatch, source, destination, String.format("thread%d",i)));
            }

            // transfer 10 source bankAccount1 destination bankAccount2 5 times
            for (int i = 0; i < 5; i++) {
                threadPool.submit(new TransferRunnableV2(countDownLatch, source, destination, String.format("thread%d",i)));
            }

            System.out.println("Waiting for transfer threads destination finish");
        }finally {
            countDownLatch.await();
            System.out.println("Finished transfer in threads");
            threadPool.shutdown();
        }
    }
}
