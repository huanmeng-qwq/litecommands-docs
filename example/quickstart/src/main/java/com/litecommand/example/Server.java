package com.litecommand.example;

import java.util.Arrays;

public enum Server {
    SERVER1("name1"),
    SERVER2("name2"),
    SERVER3("name3");

    public final String name;

    Server(String name) {
        this.name = name;
    }

    public static Server getServer(String name) {
        return Arrays.stream(Server.values()).filter(s -> s.name.equals(name)).findFirst().orElse(null);
    }
}
