package com.alessandrodias.miniredis.model;

public enum MiniRedisCommand {
    SET("SET"),
    GET("GET"),
    DEL("DEL"),
    DBSIZE("DBSIZE"),
    INCR("INCR"),
    ZADD("ZADD"),
    ZCARD("ZCARD"),
    ZRANK("ZRANK"),
    ZRANGE("ZRANGE"),
    HELP("HELP"),
    EXIT("EXIT");

    private String command;

    MiniRedisCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean equalsCommandName(String command) {
        return this.command.equals(command);
    }

    public static MiniRedisCommand getMiniRedisCommandByName(String name) {
        for (MiniRedisCommand type : values()) {
            if (type.equalsCommandName(name))
                return type;
        }
        return null;
    }
}

