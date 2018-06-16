package com.alessandrodias.miniredis.fixture;

import com.alessandrodias.miniredis.service.MiniRedisService;

import java.util.concurrent.ConcurrentHashMap;

public class MiniRedisServiceFixture {

    private MiniRedisService miniRedisService;

    public MiniRedisServiceFixture() {
        this.miniRedisService = new MiniRedisService();
    }

    public static MiniRedisServiceFixture get() {
        return new MiniRedisServiceFixture();
    }

    public MiniRedisService build() {
        return miniRedisService;
    }

    public MiniRedisServiceFixture withDatabase(ConcurrentHashMap database) {
        miniRedisService.setDatabase(database);
        return this;
    }
}
