package com.alessandrodias.miniredis.model;

import java.util.Calendar;

public class MiniRedisData {

    public static final Long NIL = null;

    private Long expiration;

    public MiniRedisData(Integer seconds) {
        this.expiration = calculateExpiration(seconds);
    }

    private Long calculateExpiration(Integer seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTimeInMillis();
    }

    public MiniRedisData() {
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public boolean isExpired(){
        if (getExpiration() != null)
            return (Calendar.getInstance().getTimeInMillis() > getExpiration());
        return false;
    }
}
