package com.alessandrodias.miniredis.model;

import java.time.Instant;

public class MiniRedisBaseData {

    public static final Long NIL = null;

    private Instant expiration;

    public MiniRedisBaseData() {
    }

    public MiniRedisBaseData(Integer seconds) {
        this.setExpiration(calculateExpiration(seconds));
    }

    public synchronized Instant getExpiration() {
        return expiration;
    }

    public synchronized void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }

    public synchronized boolean isValid(){
        if (getExpiration() != null) {
            return (Instant.now().isBefore(expiration));
        }
        return true;
    }

    private synchronized Instant calculateExpiration(Integer seconds) {
        return Instant.now().plusSeconds(seconds);
    }
}
