package com.alessandrodias.miniredis.model;

import com.alessandrodias.miniredis.fixture.MiniRedisFixture;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

public class MiniRedisTest {

    private MiniRedis miniRedis;
    private ConcurrentHashMap database = new ConcurrentHashMap();

    @Before
    public void setUp() {
        miniRedis = MiniRedisFixture.get().build();
    }

    @Test
    public void testShouldReturnSizeZeroedWhenIsEmpty() {
        assertEquals(0, miniRedis.dbSize());
    }

    @Test
    public void testReturnProperSizeWhenIsNotEmpty() {
        database.put("key", "value");
        miniRedis = MiniRedisFixture.get().withDatabase(database).build();
        assertEquals(1, miniRedis.dbSize());
    }

    @Test
    public void testShouldReturnProperSizeWhenAKeyIsSet() {
        assertEquals("OK", miniRedis.set("key", "value"));
    }

    @Test
    public void testShouldReturnProperValueWhenAKeyIsGet() {
        database.put("key", "value");
        MiniRedis miniRedis = MiniRedisFixture.get().withDatabase(database).build();
        assertEquals("value", miniRedis.get("key"));
    }

    @Test
    public void testShouldReturnNilValueWhenANonExistingKeyIsGet() {
        assertEquals(MiniRedis.NIL, miniRedis.get("key"));
    }


    @Test
    public void testShouldDeleteOneKey() {
        database.put("key", "value");
        miniRedis = MiniRedisFixture.get().withDatabase(database).build();
        assertEquals(1, miniRedis.del("key"));
    }

    @Test
    public void testShouldDeleteTwoKeys() {
        database.put("key", "value");
        database.put("key1", "value1");
        miniRedis = MiniRedisFixture.get().withDatabase(database).build();

        assertEquals(2, miniRedis.del("key", "key1"));
    }

    @Test
    public void testShouldDeleteTwoKeysAndIgnoreOneNonExistentKey() {
        database.put("key", "value");
        database.put("key1", "value1");
        miniRedis = MiniRedisFixture.get().withDatabase(database).build();

        assertEquals(2, miniRedis.del("key", "key1", "key2"));
    }

    @Test
    public void testShouldIncrementExistingNumericKey(){
        database.put("key", "1");
        miniRedis = MiniRedisFixture.get().withDatabase(database).build();

        assertEquals(2, miniRedis.incr("key"));
    }

    @Test
    public void testShouldIncrementNonExistingNumericKey(){
        assertEquals(1, miniRedis.incr("key"));
    }

    @Test(expected = NumberFormatException.class)
    public void testShouldThrowExceptionWhenIncrOfNonIntKeyIsCalled() {
        database.put("key", "a");
        miniRedis = MiniRedisFixture.get().withDatabase(database).build();

        miniRedis.incr("key");
    }





}