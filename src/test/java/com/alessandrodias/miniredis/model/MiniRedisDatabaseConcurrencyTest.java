package com.alessandrodias.miniredis.model;

import com.alessandrodias.miniredis.service.MiniRedisDatabase;
import net.jodah.concurrentunit.ConcurrentTestCase;
import org.junit.Test;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

public class MiniRedisDatabaseConcurrencyTest extends ConcurrentTestCase {

    private MiniRedisDatabase miniRedisDatabase = new MiniRedisDatabase();

    @Test
    public void testShouldIncreaseBaseDataStringThreadSafe() throws TimeoutException {
        new Thread(() -> {
            miniRedisDatabase.incr("X");
            threadAssertTrue(true);
            assertEquals("1", miniRedisDatabase.get("X"));
            resume();
        }).start();

        await(1000);

        new Thread(() -> {
            miniRedisDatabase.incr("X");
            threadAssertTrue(true);
            assertEquals("2", miniRedisDatabase.get("X"));
            resume();
        }).start();
    }

    @Test
    public void testShouldSetBaseDataStringThreadSafe() throws TimeoutException {
        new Thread(() -> {
            miniRedisDatabase.set("X", "1");
            threadAssertTrue(true);
            assertEquals("1", miniRedisDatabase.get("X"));
            resume();
        }).start();

        await(1000);

        new Thread(() -> {
            miniRedisDatabase.incr("X");
            threadAssertTrue(true);
            assertEquals("2", miniRedisDatabase.get("X"));
            resume();
        }).start();
    }

}