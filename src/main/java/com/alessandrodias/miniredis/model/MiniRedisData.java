package com.alessandrodias.miniredis.model;

import java.time.Instant;
import java.util.Calendar;

public class MiniRedisData {

    public static final Long NIL = null;

    private Instant expiration;

    public MiniRedisData() {
    }

    public MiniRedisData(Integer seconds) {
        this.setExpiration(calculateExpiration(seconds));
    }

    public Instant getExpiration() {
        return expiration;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }

    public boolean isValid(){
        if (expiration != null) {
            return (Instant.now().isBefore(expiration));
        }
        return true;
    }

    private Instant calculateExpiration(Integer seconds) {
        return Instant.now().plusSeconds(seconds);
    }
}
