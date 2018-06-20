package com.alessandrodias.miniredis.model;

public class MiniRedisBaseDataString extends MiniRedisBaseData {

    private String value;

    public MiniRedisBaseDataString(String value) {
        this.setValue(value);
    }

    public MiniRedisBaseDataString(String value, Integer expiration) {
        super(expiration);
        this.setValue(value);
    }

    public synchronized String getValue() {
        return value;
    }

    public synchronized void setValue(String value) {
        this.value = value;
    }
}
