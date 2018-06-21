package com.alessandrodias.miniredis.service;

import java.util.concurrent.ConcurrentHashMap;

public class MiniRedisService {

    private ConcurrentHashMap<String, ConcurrentHashMap> userDatabases = new ConcurrentHashMap<>();

    public synchronized ConcurrentHashMap getUserDatabaseByUser(String user){
        return userDatabases.get(user);
    }

    public synchronized void addUserDatabase(String user) {
    }

}
