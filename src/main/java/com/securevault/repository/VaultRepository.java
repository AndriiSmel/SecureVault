package com.securevault.repository;

import com.securevault.model.VaultEntry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VaultRepository {
    public boolean addEntry(int userId, String serviceName, String login, String website, String password, String nonce) {
        String sql = "INSERT INTO vault_entries (user_id, service_name, login, website, password, nonce) VALUES (?, ?, ?, ?, ?, ?)";

        try (var conn = Database.openConnection();
             var statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, serviceName);
            statement.setString(3, login);
            statement.setString(4, website);
            statement.setString(5, password);
            statement.setString(6, nonce);

            int success = statement.executeUpdate();
            return success == 1;
        } catch (SQLException e) {
            throw new IllegalStateException("Could not save entry.", e);
        }
    }
    public List<VaultEntry> getUserEntries(int userId) {
        String sql = "SELECT id, user_id, service_name, login, website, password, nonce FROM vault_entries WHERE user_id = ?";

        List<VaultEntry> entries = new ArrayList<>();
        try (var conn = Database.openConnection();
             var statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (var result = statement.executeQuery()) {
                while (result.next()) {
                    VaultEntry entry = new VaultEntry (result.getInt("id"),
                            result.getInt("user_id"),
                            result.getString("service_name"),
                            result.getString("login"),
                            result.getString("website"),
                            result.getString("password"),
                            result.getString("nonce"));
                    entries.add(entry);
                }
                return entries;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not load vault entries.", e);
        }
    }
    public boolean deleteEntry(int userId, int entryId) {
        String sql = "DELETE FROM vault_entries WHERE user_id = ? AND id = ?";

       try (var conn = Database.openConnection();
            var statement = conn.prepareStatement(sql)) {
           statement.setInt(1, userId);
           statement.setInt(2, entryId);
           int rowDeleted = statement.executeUpdate();
            return rowDeleted == 1;
        }
        catch (SQLException e){
            throw new IllegalStateException("Could not delete vault entry.", e);
        }
    }
}
