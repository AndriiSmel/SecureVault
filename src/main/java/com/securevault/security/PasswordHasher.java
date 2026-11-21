package com.securevault.security;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.security.MessageDigest;

public class PasswordHasher {
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 600000;
    private static final int KEY_LENGTH = 256;
    public static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }
    private static byte[] derive(char[] password, byte[] salt, int iterations ,  int keyLength) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            SecretKey key = factory.generateSecret(spec);
            return key.getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        finally {
            spec.clearPassword();
        }
    }

    public static String hashPassword(char[] password) {
        byte [] salt = generateSalt();

        byte [] hash = derive(password, salt, ITERATIONS, KEY_LENGTH);
        Base64.Encoder encoder = Base64.getEncoder();
        String base64Salt = encoder.encodeToString(salt);
        String base64Hash = encoder.encodeToString(hash);

        return "pbkdf2-sha256:" + ITERATIONS + ":" + base64Salt + ":" + base64Hash;
    }
    public static boolean verifyPassword(char [] password, String storedHash) {
        if (storedHash == null || storedHash.isBlank()) {System.out.println("Invalid stored hash.");return false;}
        String[] hashes = storedHash.split(":");
        if (hashes.length != 4) {System.out.println("Invalid stored hash.");return false;}
        if (!hashes[0].equals("pbkdf2-sha256")){
            System.out.println("Unknown hashing algorithm.");
            return false;
        }
        try{
            int iterations = Integer.parseInt(hashes[1]);
            byte [] decodedSalt= Base64.getDecoder().decode(hashes[2]);
            byte [] decodedHash= Base64.getDecoder().decode(hashes[3]);
            if (decodedSalt.length != SALT_LENGTH){ return false; }
            if (decodedHash.length != KEY_LENGTH/8){ return false; }
            if (iterations != ITERATIONS){ return false; }
            byte [] newHash = derive(password, decodedSalt, iterations, KEY_LENGTH);
            return MessageDigest.isEqual(decodedHash, newHash);
        }
        catch (IllegalArgumentException e){
            System.out.println("Invalid hash string.");
            return false;
        }
    }
}
