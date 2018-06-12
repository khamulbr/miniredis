package com.alessandrodias.miniredis.model;


import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MiniRedis {


    public static final Long NIL = null;

    private ConcurrentHashMap<String, Object> database = new ConcurrentHashMap();

    public int dbSize() {
        return database.size();
    }

    public void setDatabase(ConcurrentHashMap database) {
        this.database = database;
    }

    public String set(String key, String value) {
        this.database.put(key, value);
        return "OK";
    }

    public String get(String key) {
        Object value = database.get(key);
        if (value != null) {
            if (value instanceof String) {
                return (String) value;
            } else {
                throw new RuntimeException("Invalid String");
            }
        } else
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