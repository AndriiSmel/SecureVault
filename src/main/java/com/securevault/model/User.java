package com.securevault.model;

public class User {
    private final int id;
    private final String username;
    private String passwordHash;
    public User(int StartId, String startUsername, String startPasswordHash) {
        id = StartId;
        username = startUsername;
        passwordHash = startPasswordHash;
    }

    public void setPasswordHash(String newValue) {
        passwordHash = newValue;
    }

    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getPasswordHash() {
        return passwordHash;
    }

}