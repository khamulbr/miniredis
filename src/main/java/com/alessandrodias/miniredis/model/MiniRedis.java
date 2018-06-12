package com.alessandrodias.miniredis.model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MiniRedis {


    public static final Long NIL = null;

    private ConcurrentHashMap<String, String> database = new ConcurrentHashMap();

    public int dbSize() {
        return database.size();
    }

    public void setDatabase(ConcurrentHashMap database) {
        this.database = database;
    }

    public void set(String key, String value) {
        this.database.put(key, value);
    }

    public String get(String key) {
        return this.database.get(key);
    }

    public int delete(String... keys) {
        Set mySet = new HashSet(Arrays.asList(keys));
        int deletedKeys = database.entrySet().stream().filter(p->mySet.contains(p.getKey())).collect(Collectors.toList()).size();
        database.keySet().removeAll(mySet);
        return deletedKeys;
    }
}
