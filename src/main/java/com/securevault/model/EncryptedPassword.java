package com.securevault.model;

public class EncryptedPassword {
    private final String encryptedPassword;
    private final String nonce;

    public EncryptedPassword(String newEncryptedPassword, String newNonce) {
        encryptedPassword = newEncryptedPassword;
        nonce = newNonce;
    }

    public String getEncryptedPassword() {return encryptedPassword;}
    public String getNonce() {return nonce;}
}


