package com.alessandrodias.miniredis.model;

public class MiniRedisString extends MiniRedisData {

    private String value;

    public MiniRedisString(String value) {
        super();
        this.value = value;
    }

    public MiniRedisString(String value, Integer expiration) {
        super(expiration);
        new MiniRedisString(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
