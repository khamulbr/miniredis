package com.alessandrodias.miniredis.fixture;

import com.alessandrodias.miniredis.model.MiniRedis;

import java.util.concurrent.ConcurrentHashMap;

public class MiniRedisFixture {

    private MiniRedis miniRedis;

    public MiniRedisFixture() {
        this.miniRedis = new MiniRedis();
    }

    public static MiniRedisFixture get() {
        return new MiniRedisFixture();
    }

    public MiniRedis build() {
        return miniRedis;
    }

    public MiniRedisFixture withDatabase(ConcurrentHashMap database) {
        miniRedis.setDatabase(database);
        return this;
    }
}
