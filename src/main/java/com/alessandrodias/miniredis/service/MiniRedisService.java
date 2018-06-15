package com.alessandrodias.miniredis.service;


import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MiniRedisService {


    public static final Long NIL = null;

    private ConcurrentHashMap<String, Object> database = new ConcurrentHashMap();
    private ConcurrentHashMap<String, Long> databaseExpiration = new ConcurrentHashMap();

    public int dbSize() {
        return database.size();
    }

    public void setDatabase(ConcurrentHashMap database) {
        this.database = database;
    }

    public void setDatabaseExpiration(ConcurrentHashMap databaseExpiration) {
        this.databaseExpiration = databaseExpiration;
    }

    public String set(String key, String value) {
        this.database.put(key, value);
        return "OK";
    }


    public String set(String key, String value, Integer seconds) {
        if (seconds < 1)
            throw new RuntimeException("(error) ERR invalid expire time in set");

        this.databaseExpiration.put(key, setExpirationTime(seconds));
        this.set(key, value);
        return "OK";
    }

    private Long setExpirationTime(Integer seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTimeInMillis();
    }

    private Long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public String get(String key) {
        Object value = database.get(key);
        if (databaseExpiration.get(key) == null || (getCurrentTime().compareTo(databaseExpiration.get(key)) == -1)) {
            if (value != null) {
                if (value instanceof String) {
                    return (String) value;
                } else {
                    throw new RuntimeException("Invalid String");
                }
            }
        }
       return null;
    }

    public int del(String... keys) {
        Set mySet = new HashSet(Arrays.asList(keys));
        int deletedKeys = database.entrySet().stream().filter(p->mySet.contains(p.getKey())).collect(Collectors.toList()).size();
        database.keySet().removeAll(mySet);
        return deletedKeys;
    }

    public int incr(String key) {
        Integer valueToIncrement = 0;
        String value = get(key);
        if (value != null) {
            if (StringUtils.isNumeric(value)) {
                valueToIncrement = Integer.parseInt(value);
            } else {
                throw new NumberFormatException("(error) ERR value is not an integer or out of range");
            }
        }
        valueToIncrement++;
        set(key, String.valueOf(valueToIncrement));
        return valueToIncrement;
    }

}