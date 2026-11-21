package com.securevault.model;

public class User {
    private final int id;
    private final String username;
    private String passwordHash;
    private final String encryption_salt;
    public User(int StartId, String startUsername, String startPasswordHash, String startEncryptionSalt) {
        id = StartId;
        username = startUsername;
        passwordHash = startPasswordHash;
        encryption_salt = startEncryptionSalt;
    }

    public int getId() {
        return id;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public String getEncryptionSalt() {return encryption_salt;}

}