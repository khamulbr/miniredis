package com.alessandrodias.miniredis.service;


import com.alessandrodias.miniredis.model.MiniRedisData;
import com.alessandrodias.miniredis.model.MiniRedisDataOrderedSet;
import com.alessandrodias.miniredis.model.MiniRedisDataSet;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MiniRedisService {


    public static final Long NIL = null;

    private ConcurrentHashMap<String, MiniRedisData> database = new ConcurrentHashMap();

    public int dbSize() {
        return database.size();
    }

    public void setDatabase(ConcurrentHashMap database) {
        this.database = database;
    }


    public String set(String key, String value) {
        this.database.put(key, new MiniRedisDataSet(value));
        return "OK";
    }


    public String set(String key, String value, Integer secondsToExpire) {
        if (secondsToExpire < 1)
            throw new RuntimeException("(error) ERR invalid expire time in set");

        this.database.put(key, new MiniRedisDataSet(value, secondsToExpire));
        return "OK";
    }
    

    private Long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public String get(String key) {
        MiniRedisDataSet miniRedisData = (MiniRedisDataSet) database.get(key);
        if (miniRedisData != null) {
            System.out.println("expiration from db = " + miniRedisData.getExpiration());
            System.out.println("time               = " + getCurrentTime());
            if (!miniRedisData.isExpired()) {
                return miniRedisData.getValue();
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

    public void zadd(String key, Double score, String value) {
        database.put(key, new MiniRedisDataOrderedSet(score, value));
    }

}