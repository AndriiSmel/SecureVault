package com.securevault.service;

import com.securevault.model.EncryptedPassword;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

public class EncryptionService {
    private static final int ITERATIONS = 600000;
    private static final int KEY_LENGTH = 256;

    public SecretKey deriveKey(String password, String salt) {
        byte [] saltByte = Base64.getDecoder().decode(salt);
        char [] passwordarray = password.toCharArray();
        PBEKeySpec spec = new PBEKeySpec(passwordarray, saltByte, ITERATIONS, KEY_LENGTH);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        finally {
            spec.clearPassword();
            Arrays.fill(passwordarray, '\0');
        }
    }

    public EncryptedPassword encrypt(String password, SecretKey key) {
        byte [] nonce = new byte[12];
        SecureRandom random = new SecureRandom();
        random.nextBytes(nonce);
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, nonce);
            cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);
            byte [] encryptedPassword = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
            return new EncryptedPassword(Base64.getEncoder().encodeToString(encryptedPassword), Base64.getEncoder().encodeToString(nonce));
        }
        catch ( GeneralSecurityException e ){
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String encryptedpassword, String nonce, SecretKey key) {
        byte [] encryptedPasswordBytes = Base64.getDecoder().decode(encryptedpassword);
        byte [] nonceBytes = Base64.getDecoder().decode(nonce);
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, nonceBytes);
            cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);
            byte [] decryptedPassword = cipher.doFinal(encryptedPasswordBytes);
            return new String(decryptedPassword, StandardCharsets.UTF_8);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
