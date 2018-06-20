package com.alessandrodias.miniredis.fixture;

import com.alessandrodias.miniredis.model.MiniRedisBaseData;

import java.time.Instant;

public class MiniRedisBaseDataFixture {

    private MiniRedisBaseData miniRedisBaseData;

    public MiniRedisBaseDataFixture() {
        this.miniRedisBaseData = new MiniRedisBaseData();
    }

    public static MiniRedisBaseDataFixture get() {
        return new MiniRedisBaseDataFixture();
    }

    public MiniRedisBaseDataFixture withExpiration(Instant expiration) {
        miniRedisBaseData.setExpiration(expiration);
        return this;
    }

    public MiniRedisBaseData build() {
        return miniRedisBaseData;
    }
}
