package com.securevault;
import com.securevault.repository.UserRepository;
import com.securevault.repository.VaultRepository;
import com.securevault.service.AuthService;
import com.securevault.service.EncryptionService;
import com.securevault.ui.MainMenu;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;


public class App 
{
    public static void connect() {
        String url = "jdbc:sqlite:securevault.db";

        try (var conn = DriverManager.getConnection(url);
             var statement = conn.createStatement()) {

            statement.execute("PRAGMA foreign_keys = ON");
            statement.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY,
                    username TEXT NOT NULL UNIQUE,
                    password_hash TEXT NOT NULL,
                    encryption_salt TEXT NOT NULL);""");

            statement.execute("""
                CREATE TABLE IF NOT EXISTS vault_entries (
                    id INTEGER PRIMARY KEY,
                    user_id INTEGER NOT NULL REFERENCES users(id),
                    service_name TEXT NOT NULL,
                    login TEXT NOT NULL,
                    website TEXT,
                    password TEXT NOT NULL,
                    nonce TEXT NOT NULL);
                """);
            try (var result = statement.executeQuery("PRAGMA foreign_keys")) {
                if (!result.next() || result.getInt(1) != 1) {
                    throw new SQLException("SQLite foreign key enforcement is off.");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQLite setup failed: " + e.getMessage());
        }
    }
    public static void main( String[] args ){
        connect();
        Scanner scanner = new Scanner(System.in);
        System.out.println("=".repeat(32));
        System.out.println( "Welcome to SECUREVAULT \n v1.0 by Andrii" );
        System.out.println( "A local, encrypted password manager." );
        System.out.println("=".repeat(32));
        UserRepository userRepository = new UserRepository();
        VaultRepository vaultRepository = new VaultRepository();
        EncryptionService encryptionService = new EncryptionService();
        AuthService authService = new AuthService(userRepository, encryptionService);
        MainMenu menu = new MainMenu(authService, vaultRepository,  encryptionService);
        menu.mainMenu(scanner);
    }
}
