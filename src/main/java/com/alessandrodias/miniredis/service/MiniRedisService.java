package com.alessandrodias.miniredis.service;


import com.alessandrodias.miniredis.model.MiniRedisData;
import com.alessandrodias.miniredis.model.MiniRedisDataOrderedSet;
import com.alessandrodias.miniredis.model.MiniRedisString;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MiniRedisService {


    private ConcurrentHashMap<String, MiniRedisData> database = new ConcurrentHashMap();

    public int dbSize() {
        return database.size();
    }

    public void setDatabase(ConcurrentHashMap database) {
        this.database = database;
    }


    public String set(String key, String value) {
        this.database.put(key, new MiniRedisString(value));
        return "OK";
    }


    public String set(String key, String value, Integer secondsToExpire) {
        if (secondsToExpire < 1)
            throw new RuntimeException("(error) ERR invalid expire time in set");

        this.database.put(key, new MiniRedisString(value, secondsToExpire));
        return "OK";
    }
    

    private Long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public String get(String key) {
        MiniRedisString miniRedisData = (MiniRedisString) database.get(key);
        if (miniRedisData != null) {
            if (miniRedisData.isValid()) {
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

    private MiniRedisDataOrderedSet getMiniRedisDataOrderedSetFromDatabaseKey(String key) {
        return (MiniRedisDataOrderedSet) database.get(key);

    }

    public int zadd(String key, Double score, String value) {
        MiniRedisDataOrderedSet miniRedisData = getMiniRedisDataOrderedSetFromDatabaseKey(key);
        if (miniRedisData == null) {
            miniRedisData = new MiniRedisDataOrderedSet();
        }
        int returnValue = miniRedisData.putScoredMember(score, value);
        if (returnValue == 1)
            database.put(key, miniRedisData);
        return returnValue;
    }

    public int zCard(String key) {
        MiniRedisDataOrderedSet miniRedisDataOrderedSetFromKey = getMiniRedisDataOrderedSetFromDatabaseKey(key);
        if (miniRedisDataOrderedSetFromKey != null)
            return getMiniRedisDataOrderedSetFromDatabaseKey(key).getScoredMemberSize();
        return 0;
    }

    public Integer zRank(String key, String value) {
        MiniRedisDataOrderedSet miniRedisDataOrderedSetFromKey = getMiniRedisDataOrderedSetFromDatabaseKey(key);
        if (miniRedisDataOrderedSetFromKey != null)
            return miniRedisDataOrderedSetFromKey.getScoredMemberRank(value);
        return null;
    }

    public List<String> zRange(String key, int start, int stop) {
        MiniRedisDataOrderedSet miniRedisDataOrderedSetFromKey = getMiniRedisDataOrderedSetFromDatabaseKey(key);
        if (miniRedisDataOrderedSetFromKey != null)
            return (getMiniRedisDataOrderedSetFromDatabaseKey(key)).getScoredMemberInRange(start, stop);
        return null;
    }
}