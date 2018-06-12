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
        database.put("test", "test");
    }

    @Test
    public void testShouldReturnSizeZeroedWhenIsEmpty() {
        assertEquals(0, miniRedis.dbSize());
    }

    @Test
    public void testReturnProperSizeWhenIsNotEmpty() {
        MiniRedis miniRedis = MiniRedisFixture.get().withDatabase(database).build();
        assertEquals(1, miniRedis.dbSize());
    }

    @Test
    public void testShouldReturnProperSizeWhenAKeyIsSet() {
        miniRedis.set("key", "value");
        assertEquals(1, miniRedis.dbSize());
    }

    @Test
    public void testShouldReturnProperValueWhenAKeyIsGet() {
        miniRedis.set("key", "value");
        assertEquals("value", miniRedis.get("key"));
    }

    @Test
    public void testShouldReturnNilValueWhenANonExistingKeyIsGet() {
        assertEquals(MiniRedis.NIL, miniRedis.get("key"));
    }

    @Test
    public void testShouldDeleteOneKey() {
        miniRedis.set("key", "value");
        assertEquals(1, miniRedis.delete("key"));
    }

    @Test
    public void testShouldDeleteTwoKeys() {
        miniRedis.set("key", "value");
        miniRedis.set("key1", "value1");
        assertEquals(2, miniRedis.delete("key", "key1"));
    }

    @Test
    public void testShouldDeleteTwoKeysAndIgnoreOneNonExistentKey() {
        miniRedis.set("key", "value");
        miniRedis.set("key1", "value1");
        assertEquals(2, miniRedis.delete("key", "key1", "key2"));
    }



}
