package com.alessandrodias.miniredis.service;

import net.jodah.concurrentunit.ConcurrentTestCase;
import org.junit.After;
import org.junit.Test;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

public class MiniRedisServiceConcurrencyTest extends ConcurrentTestCase {

    private MiniRedisService miniRedisService = new MiniRedisService();

    @Test
    public void testShouldIncreaseBaseDataStringThreadSafe() throws TimeoutException {
        new Thread(() -> {
            miniRedisService.incr("X");
            threadAssertTrue(true);
            assertEquals("1", miniRedisService.get("X"));
            resume();
        }).start();

        await(1000);

        new Thread(() -> {
            miniRedisService.incr("X");
            threadAssertTrue(true);
            assertEquals("2", miniRedisService.get("X"));
            resume();
        }).start();
    }

    @Test
    public void testShouldSetBaseDataStringThreadSafe() throws TimeoutException {
        new Thread(() -> {
            miniRedisService.set("X", "1");
            threadAssertTrue(true);
            assertEquals("1", miniRedisService.get("X"));
            resume();
        }).start();

        await(1000);

        new Thread(() -> {
            miniRedisService.incr("X");
            threadAssertTrue(true);
            assertEquals("2", miniRedisService.get("X"));
            resume();
        }).start();
    }

}