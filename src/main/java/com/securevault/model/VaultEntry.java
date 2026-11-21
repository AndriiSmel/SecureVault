package com.securevault.model;

public class VaultEntry {
    private final int id;
    private final int userId;
    private String serviceName;
    private String website;
    private String login;
    private String password;
    private String nonce;

    public VaultEntry(int entryid, int startUserId, String newserviceName, String newlogin, String newwebsite,  String newpassword, String newnonce) {
        id = entryid;
        userId = startUserId;
        serviceName = newserviceName;
        login = newlogin;
        website = newwebsite;
        password = newpassword;
        nonce = newnonce;
    }

    public int getId() {
        return id;
    }
    public String getServiceName() {
        return serviceName;
    }
    public String getWebsite() {
        return website;
    }
    public String getLogin() {
        return login;
    }
    public String getPassword() {return password;}
    public String getNonce() {return nonce;}
}
