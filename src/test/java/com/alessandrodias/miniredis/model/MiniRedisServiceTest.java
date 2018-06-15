package com.alessandrodias.miniredis.model;

import com.alessandrodias.miniredis.fixture.MiniRedisServiceFixture;
import com.alessandrodias.miniredis.service.MiniRedisService;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

public class MiniRedisServiceTest {

    private MiniRedisService miniRedisService;
    private ConcurrentHashMap database = new ConcurrentHashMap();
    private ConcurrentHashMap databaseExpiration = new ConcurrentHashMap();
    private Calendar calendar;

    @Before
    public void setUp() {
        miniRedisService = MiniRedisServiceFixture.get().build();
        calendar = Calendar.getInstance();
    }

    @Test
    public void testShouldReturnSizeZeroedWhenIsEmpty() {
        assertEquals(0, miniRedisService.dbSize());
    }

    @Test
    public void testReturnProperSizeWhenIsNotEmpty() {
        database.put("key", "value");
        miniRedisService = MiniRedisServiceFixture.get().withDatabase(database).build();
        assertEquals(1, miniRedisService.dbSize());
    }

    @Test
    public void testShouldReturnProperSizeWhenAKeyIsSet() {
        assertEquals("OK", miniRedisService.set("key", "value"));
    }

    @Test
    public void testShouldReturnProperValueWhenAKeyIsGet() {
        database.put("key", "value");
        MiniRedisService miniRedisService = MiniRedisServiceFixture.get().withDatabase(database).build();
        assertEquals("value", miniRedisService.get("key"));
    }

    @Test
    public void testShouldReturnNilValueWhenANonExistingKeyIsGet() {
        assertEquals(MiniRedisService.NIL, miniRedisService.get("key"));
    }


    @Test
    public void testShouldDeleteOneKey() {
        database.put("key", "value");
        miniRedisService = MiniRedisServiceFixture.get().withDatabase(database).build();
        assertEquals(1, miniRedisService.del("key"));
    }

    @Test
    public void testShouldDeleteTwoKeys() {
        database.put("key", "value");
        database.put("key1", "value1");
        miniRedisService = MiniRedisServiceFixture.get().withDatabase(database).build();

        assertEquals(2, miniRedisService.del("key", "key1"));
    }

    @Test
    public void testShouldDeleteTwoKeysAndIgnoreOneNonExistentKey() {
        database.put("key", "value");
        database.put("key1", "value1");
        miniRedisService = MiniRedisServiceFixture.get().withDatabase(database).build();

        assertEquals(2, miniRedisService.del("key", "key1", "key2"));
    }

    @Test
    public void testShouldIncrementExistingNumericKey(){
        database.put("key", "1");
        miniRedisService = MiniRedisServiceFixture.get().withDatabase(database).build();

        assertEquals(2, miniRedisService.incr("key"));
    }

    @Test
    public void testShouldIncrementNonExistingNumericKey(){
        assertEquals(1, miniRedisService.incr("key"));
    }

    @Test(expected = NumberFormatException.class)
    public void testShouldThrowExceptionWhenIncrOfNonIntKeyIsCalled() {
        database.put("key", "a");
        miniRedisService = MiniRedisServiceFixture.get().withDatabase(database).build();

        miniRedisService.incr("key");
    }

    @Test
    public void testShouldCreateAExpirableKeyAndGetValueWithinTime() throws InterruptedException {
        miniRedisService.set("key", "a", 1);

        Thread.sleep(500);
        assertEquals("a", miniRedisService.get("key"));
    }

    @Test
    public void testShouldCreateAExpirableKeyAndGetNilAsExpireTimeHasPassed() throws InterruptedException {
        miniRedisService.set("key", "a", 1);

        Thread.sleep(1500);
        assertEquals(MiniRedisService.NIL, miniRedisService.get("key"));
    }

    @Test(expected = RuntimeException.class)
    public void testShouldNotCreateAExpirableKeyWhenExpirationIsZero() throws InterruptedException {
        miniRedisService.set("key", "a", 0);
    }



}