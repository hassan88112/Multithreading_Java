package org.example.deadlock;

import org.example.atomicity.BankAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class TransferRunnableV2 implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferRunnableV2.class);

    private final CountDownLatch countDownLatch;
    private final BankAccount source;
    private final BankAccount destination;
    private final String threadName;

    public TransferRunnableV2(CountDownLatch countDownLatch, BankAccount source, BankAccount destination, String threadName) {
        this.countDownLatch = countDownLatch;
        this.source = source;
        this.destination = destination;
        this.threadName = threadName;
    }

    @Override
    public void run() {
        MDC.put("threadName", threadName);
        try {
            LOGGER.info("Running then sleep random <100 millisec ...");
            randomSleep();
            LOGGER.info("locking destination");
            synchronized (destination) {
                LOGGER.info("destination locked!");
                LOGGER.info("locking source");
                synchronized (source) {
                    LOGGER.info("source locked!");
                    LOGGER.info("Transferring 10 from source to destination");
                    source.subtract(10);
                    destination.add(10);
                }
            }
        }
        finally {
            countDownLatch.countDown();
        }
    }

    private static void randomSleep() {
        int randomSleep = new SecureRandom().nextInt(0, 100);
        try {
            LOGGER.info("sleeping for {} milliseconds", randomSleep);
            Thread.sleep(randomSleep);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
