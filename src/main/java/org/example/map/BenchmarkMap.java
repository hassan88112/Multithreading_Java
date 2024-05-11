package org.example.map;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BenchmarkMap {
    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkMap.class);
    public static final int MAX_NUMBER = 10_000_000;

    public static void main(String [] args) throws InterruptedException {

        Map<Integer, Integer> synchronizedMap = Collections.synchronizedMap(new HashMap<>());
        Map<Integer, Integer> concurrentMap = new ConcurrentHashMap();

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        testMapPerformance(synchronizedMap);
        stopWatch.stop();
        LOGGER.info("It took synchronized map to {} fill {} values", stopWatch, MAX_NUMBER);

        stopWatch = new StopWatch();
        stopWatch.start();
        testMapPerformance(concurrentMap);
        stopWatch.stop();
        LOGGER.info("It took concurrent map to {} fill {} values", stopWatch, MAX_NUMBER);
    }

    private static void testMapPerformance(Map<Integer, Integer> map) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        SecureRandom randomizer = new SecureRandom();

        try {
            for(int i=0;i<10;i++) {
                threadPool.submit(() -> {
                    while (map.size() < MAX_NUMBER) {
                        int random = randomizer.nextInt(0, MAX_NUMBER);
                        map.putIfAbsent(random, random);
                    }
                    countDownLatch.countDown();
                });
            }

            countDownLatch.await();
        }
        finally {
            threadPool.shutdown();
        }
    }
}
