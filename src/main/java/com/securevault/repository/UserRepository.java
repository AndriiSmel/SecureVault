package com.securevault.repository;

import com.securevault.model.User;
import java.sql.SQLException;

public class UserRepository {
    public void addUser(String username, String passwordHash, String salt) {
        String sql = "INSERT INTO users (username, password_hash, encryption_salt) VALUES (?, ?, ?)";

        try (var conn = Database.openConnection();
             var statement = conn.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, passwordHash);
            statement.setString(3, salt);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not save user.", e);
        }
    }

    public User searchByUsername(String username) {
        String sql = "SELECT id, username, password_hash, encryption_salt FROM users WHERE username = ?";

        try (var conn = Database.openConnection();
             var statement = conn.prepareStatement(sql)) {
            statement.setString(1, username);

            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    return new User(
                            result.getInt("id"),
                            result.getString("username"),
                            result.getString("password_hash"),
                            result.getString("encryption_salt"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not look up user.", e);
        }
    }
}