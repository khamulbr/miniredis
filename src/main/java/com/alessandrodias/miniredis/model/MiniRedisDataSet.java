package com.alessandrodias.miniredis.model;

public class MiniRedisDataSet extends MiniRedisData {

    private String value;

    public MiniRedisDataSet(String value) {
        super();
        this.value = value;
    }

    public MiniRedisDataSet(String value, Integer expiration) {
        super(expiration);
        new MiniRedisDataSet(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
