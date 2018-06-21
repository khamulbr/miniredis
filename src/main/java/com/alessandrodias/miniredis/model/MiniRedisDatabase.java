package com.alessandrodias.miniredis.model;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@SessionScope
public class MiniRedisDatabase {

    private ConcurrentHashMap<String, MiniRedisBaseData> database = new ConcurrentHashMap();

    public synchronized int dbSize() {
        return database.size();
    }

    public synchronized void setDatabase(ConcurrentHashMap database) {
        this.database = database;
    }

    public synchronized String set(String key, String value) {
        this.database.put(key, new MiniRedisBaseDataString(value));
        return "OK";
    }

    public synchronized String set(String key, String value, Integer secondsToExpire) {
        if (secondsToExpire < 1)
            throw new RuntimeException("(error) ERR invalid expire time in set");

        this.database.put(key, new MiniRedisBaseDataString(value, secondsToExpire));
        return "OK";
    }

    private synchronized Long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public synchronized String get(String key) {
        MiniRedisBaseDataString miniRedisData = (MiniRedisBaseDataString) database.get(key);
        if (miniRedisData != null) {
            if (miniRedisData.isValid()) {
                return miniRedisData.getValue();
            }
        }
       return null;
    }

    public synchronized int del(String... keys) {
        Set mySet = new HashSet(Arrays.asList(keys));
        int deletedKeys = database.entrySet().stream().filter(p->mySet.contains(p.getKey())).collect(Collectors.toList()).size();
        database.keySet().removeAll(mySet);
        return deletedKeys;
    }

    public synchronized int incr(String key) {
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

    private synchronized MiniRedisBaseDataOrderedSet getMiniRedisDataOrderedSetFromDatabaseKey(String key) {
        return (MiniRedisBaseDataOrderedSet) database.get(key);

    }

    public synchronized int zadd(String key, Double score, String value) {
        MiniRedisBaseDataOrderedSet miniRedisData = getMiniRedisDataOrderedSetFromDatabaseKey(key);
        if (miniRedisData == null) {
            miniRedisData = new MiniRedisBaseDataOrderedSet();
        }
        int returnValue = miniRedisData.putScoredMember(score, value);
        if (returnValue == 1)
            database.put(key, miniRedisData);
        return returnValue;
    }

    public synchronized int zCard(String key) {
        MiniRedisBaseDataOrderedSet miniRedisDataOrderedSetFromKey = getMiniRedisDataOrderedSetFromDatabaseKey(key);
        if (miniRedisDataOrderedSetFromKey != null)
            return getMiniRedisDataOrderedSetFromDatabaseKey(key).getScoredMemberSize();
        return 0;
    }

    public synchronized Integer zRank(String key, String value) {
        MiniRedisBaseDataOrderedSet miniRedisDataOrderedSetFromKey = getMiniRedisDataOrderedSetFromDatabaseKey(key);
        if (miniRedisDataOrderedSetFromKey != null)
            return miniRedisDataOrderedSetFromKey.getScoredMemberRank(value);
        return null;
    }

    public synchronized List<String> zRange(String key, int start, int stop) {
        MiniRedisBaseDataOrderedSet miniRedisDataOrderedSetFromKey = getMiniRedisDataOrderedSetFromDatabaseKey(key);
        if (miniRedisDataOrderedSetFromKey != null)
            return (getMiniRedisDataOrderedSetFromDatabaseKey(key)).getScoredMemberInRange(start, stop);
        return null;
    }
}