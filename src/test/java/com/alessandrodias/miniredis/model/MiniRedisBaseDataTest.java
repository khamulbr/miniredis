package com.alessandrodias.miniredis.model;

import com.alessandrodias.miniredis.fixture.MiniRedisBaseDataFixture;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.*;

public class MiniRedisBaseDataTest {

    @Test
    public void testShouldReturnTrueIfExpirationIsNotReached() {
        Instant expiration = Instant.now().plusSeconds(60);
        MiniRedisBaseData miniRedisBaseData = MiniRedisBaseDataFixture.get().withExpiration(expiration).build();
        assertTrue(miniRedisBaseData.isValid());
    }

    @Test
    public void testShouldReturnFalseIfExpirationIsReached() {
        Instant expiration = Instant.now().minusSeconds(60);
        MiniRedisBaseData miniRedisBaseData = MiniRedisBaseDataFixture.get().withExpiration(expiration).build();
        assertFalse(miniRedisBaseData.isValid());
    }
}