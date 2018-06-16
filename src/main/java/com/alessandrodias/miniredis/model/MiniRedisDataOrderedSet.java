package com.alessandrodias.miniredis.model;

public class MiniRedisDataOrderedSet extends MiniRedisData{

    private Double score;
    private String value;

    public MiniRedisDataOrderedSet(Double score, String value) {
        this.score = score;
        this.value = value;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
