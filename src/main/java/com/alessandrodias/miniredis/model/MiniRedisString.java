package com.alessandrodias.miniredis.model;

public class MiniRedisString extends MiniRedisData {

    private String value;

    public MiniRedisString(String value) {
        this.value = value;
    }

    public MiniRedisString(String value, Integer expiration) {
        super(expiration);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
