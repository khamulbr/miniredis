package com.alessandrodias.miniredis.fixture;

import com.alessandrodias.miniredis.service.MiniRedisDatabase;

import java.util.concurrent.ConcurrentHashMap;

public class MiniRedisServiceFixture {

    private MiniRedisDatabase miniRedisDatabase;

    public MiniRedisServiceFixture() {
        this.miniRedisDatabase = new MiniRedisDatabase();
    }

    public static MiniRedisServiceFixture get() {
        return new MiniRedisServiceFixture();
    }

    public MiniRedisDatabase build() {
        return miniRedisDatabase;
    }

    public MiniRedisServiceFixture withDatabase(ConcurrentHashMap database) {
        miniRedisDatabase.setDatabase(database);
        return this;
    }
}
