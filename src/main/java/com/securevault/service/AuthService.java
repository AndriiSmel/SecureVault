package com.securevault.service;
import com.securevault.model.User;
import com.securevault.repository.UserRepository;
import com.securevault.security.PasswordHasher;
import me.gosimple.nbvcxz.Nbvcxz;
import me.gosimple.nbvcxz.scoring.Result;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Locale;

import static com.securevault.security.PasswordHasher.hashPassword;
import static com.securevault.security.PasswordHasher.verifyPassword;


public class AuthService {
    private final UserRepository  userRepository;
    private User currentUser = null;
    private final EncryptionService encryptionService;
    private SecretKey currentKey = null;
    public AuthService(UserRepository userRepository, EncryptionService encryptionService) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
    }
    public boolean register(String username, String password) {
        if(!isValidUsername(username) || !isValidPassword(password)) {
            System.out.println("Invalid username/password");
            return false;
        }
        if(userRepository.searchByUsername(username) != null){
            System.out.println("Username already exists.");
            return false;
        }
        if(!isPasswordStrong(password) || checkPasswordCompromised(password) == BreachStatus.COMPROMISED) {
            System.out.println("Password is not strong enough/compromised.");
            return false;
        }
        String passwordHash = hashPassword(password.toCharArray());
        String salt = Base64.getEncoder().encodeToString(PasswordHasher.generateSalt());
        userRepository.addUser(username, passwordHash, salt);
        return true;
    }
    public boolean login(String username, String password) {
        if (!isValidUsername(username) || !isValidPassword(password)) {
            return false;
        }
        User user = userRepository.searchByUsername(username);
        if (user == null) {
            return false;
        }
        String passwordHash = user.getPasswordHash();
        if (!verifyPassword(password.toCharArray(), passwordHash)) {
            return false;
        }
        currentKey = encryptionService.deriveKey(password, user.getEncryptionSalt());
        currentUser = user;
        return true;
    }
    public void logout() {
        currentUser = null;
        currentKey = null;
    }

    public int getCurrentUserId() {
        if (currentUser == null) {
            throw new IllegalStateException("User not logged in");
        }
        return currentUser.getId();
    }

    public SecretKey getCurrentKey() {
        if (currentKey == null) {
            throw new IllegalStateException("User not logged in");
        }
        return currentKey;
    }

    static boolean isValidUsername(String username) {
        if (username == null){
            return false;
        }
        return username.matches("^[a-zA-Z][\\w]{2,19}$");
    }

    static boolean isValidPassword(String password) {
        if (password == null || password.isBlank()) {
            return false;
        }
        return password.matches("[\\w\\W]{15,128}$");
    }

    static boolean isPasswordStrong(String password) {
        Nbvcxz nbvcxz = new Nbvcxz();

        if (password == null){
            return false;
        }
        try {
            Result result = nbvcxz.estimate(password);
            return result.isMinimumEntropyMet();
        }
        catch (RuntimeException e) {
            System.out.println("Password strong error.");
            return false;
        }
    }

    enum BreachStatus {
        COMPROMISED, NOT_FOUND, UNKNOWN
    }
    static BreachStatus checkPasswordCompromised(String password) {
        if(password == null){return BreachStatus.UNKNOWN;}
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hashbytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            HexFormat hex = HexFormat.of();
            String hexpassword = hex.formatHex(hashbytes);
            hexpassword = hexpassword.toUpperCase(Locale.ROOT);
            String prefix = hexpassword.substring(0,5);
            String suffix = hexpassword.substring(5,40);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.pwnedpasswords.com/range/" + prefix))
                    .GET()
                    .header("Add-Padding", "true")
                    .header("User-Agent", "SecureVault-LearningProject")
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode == 200) {
                String responseBody = response.body();
                if (responseBody == null || responseBody.isBlank()) {
                    return BreachStatus.UNKNOWN;
                }
                String [] responseBodyArray = responseBody.lines().toArray(String[]::new);
                int i = 0;
                String[] temp;
                while (i < responseBodyArray.length) {
                    temp = responseBodyArray[i].split(":", -1);
                    if (temp.length != 2 || !temp[0].matches("^[0-9A-F]{35}$") || !temp[1].matches("^[0-9]+$")) {return BreachStatus.UNKNOWN;}
                    if(temp[0].equals(suffix)){
                        if (Long.parseLong(temp[1]) > 0){
                            return BreachStatus.COMPROMISED;
                        }
                    }
                    i++;
                }
                return BreachStatus.NOT_FOUND;
            }
            else{
                System.out.println("Unable to check password breach status. Please try again later. Status code: " + statusCode);
                return BreachStatus.UNKNOWN;
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Unable to check password breach status. Please try again.");
            return BreachStatus.UNKNOWN;
        } catch (IOException e) {
            System.out.println("Unable to check password breach status. Please try again.");
            return BreachStatus.UNKNOWN;
        } catch (InterruptedException e) {
            System.out.println("Unable to check password breach status. Please try again.");
            Thread.currentThread().interrupt();
            return BreachStatus.UNKNOWN;
        } catch (NumberFormatException e) {
            System.out.println("Unable to check password breach status.");
            return BreachStatus.UNKNOWN;
        }
    }
}