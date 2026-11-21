package com.securevault.ui;
import com.securevault.model.EncryptedPassword;
import com.securevault.model.VaultEntry;
import com.securevault.repository.VaultRepository;
import com.securevault.service.AuthService;
import com.securevault.service.EncryptionService;
import me.gosimple.nbvcxz.resources.Generator;

import java.util.List;
import java.util.Scanner;

public class MainMenu {
    private final AuthService authService;
    private final VaultRepository vaultRepository;
    private final EncryptionService encryptionService;

    public MainMenu(AuthService authService, VaultRepository vaultRepository, EncryptionService encryptionService) {
        this.authService = authService;
        this.vaultRepository = vaultRepository;
        this.encryptionService = encryptionService;
    }

    public void mainMenu(Scanner scanner){
        boolean exit = false;

        while (!exit){
            System.out.println("=".repeat(32));
            System.out.println("SECUREVAULT");
            System.out.println("=".repeat(32));
            System.out.println("\n 1. Register\n 2. Login\n 3. Generate Password\n 4. Exit\n");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch (choice.trim()){
                case "1": register_screen(scanner); break;
                case "2": login_screen(scanner); break;
                case "3": generatePassword_screen(scanner); break;
                case "4": terminate(); exit = true; break;
                default: System.out.println("Invalid choice."); break;
            }
        }
    }
    public void register_screen(Scanner scanner) {
        System.out.println("Selected option: 1");
        System.out.println("=".repeat(32));
        System.out.println("REGISTER");
        System.out.println("=".repeat(32));
        System.out.println("Username:");
        String username = scanner.nextLine();
        System.out.println("Password:");
        String password = scanner.nextLine();

        if (authService.register(username, password)){
            System.out.println("User registered successfully. Login to continue.");
        }
        else{System.out.println("Registration failed.");}
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    public void login_screen(Scanner scanner){
        System.out.println("Selected option: 2");
        System.out.println("=".repeat(32));
        System.out.println("LOGIN");
        System.out.println("=".repeat(32));
        while (true){
            System.out.println("Username:");
            String username = scanner.nextLine();
            System.out.println("Password:");
            String password = scanner.nextLine();

            if (authService.login(username, password)){
                System.out.println("Login successful.");
                vault_screen(scanner);return;
            }
            else {
                System.out.println("Invalid username or password.");

                boolean retrySelected = false;
                while (!retrySelected) {
                    System.out.println("\n1. Try again\n2. Back to main menu\n");
                    System.out.print("Enter your choice: ");
                    String choice = scanner.nextLine().trim();

                    switch (choice) {
                        case "1": retrySelected = true; break;
                        case "2": return;
                        default: System.out.println("Invalid choice.");
                    }
                }
            }
        }
    }

    public void vault_screen(Scanner scanner){
        System.out.println("SecureVault - My Vault");
        System.out.println("=".repeat(32));

        boolean loop = false;
        while (!loop) {
            System.out.println("1. List entries\n2. Add entry\n3. Delete entry\n4. Log out");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": listEntriesScreen(); break;
                case "2": addEntryScreen(scanner);break;
                case "3": deleteEntryScreen(scanner); break;
                case "4": authService.logout(); System.out.println("Logout successful."); loop = true; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    public void listEntriesScreen(){
        System.out.println("Selected option: 1");
        System.out.println("=".repeat(32));
        displayEntries(authService.getCurrentUserId());
    }
    public boolean displayEntries(int userId){
        List<VaultEntry> entries = vaultRepository.getUserEntries(userId);
        if (entries.isEmpty()){
            System.out.println("No entries found.");
            return false;
        }
        for (VaultEntry entry : entries) {
            try {
                String decryptedPassword = encryptionService.decrypt(entry.getPassword(), entry.getNonce(),authService.getCurrentKey());
                System.out.println("ID: " + entry.getId() + " | Service: " + entry.getServiceName() + " | Login: " + entry.getLogin() + " | URL: " + entry.getWebsite() + "| Password: " + decryptedPassword);
            }
            catch (RuntimeException e){System.out.println("Decryption failed.");}
        }
        return true;
    }

    public void addEntryScreen(Scanner scanner){
        System.out.println("Selected option: 2");
        System.out.println("=".repeat(32));

        while (true) {
            System.out.println("To quit enter \":cancel\" at any point");
            System.out.println("\nEnter Service Name: for ex.: Github, Google, etc.");
            String serviceName = scanner.nextLine();
            if (serviceName.equals(":cancel")){
                break;
            }
            if (serviceName.isBlank()){System.out.println("Invalid service name.");continue;}
            System.out.println("Enter Service Login:");
            String login = scanner.nextLine();
            if (login.equals(":cancel")){
                break;
            }
            if (login.isBlank()){System.out.println("Invalid login.");continue;}
            System.out.println("Enter Service Password:");
            String password = scanner.nextLine();
            if (password.equals(":cancel")){
                break;
            }
            if (password.isBlank()){System.out.println("Invalid password.");continue;}
            System.out.println("Enter Service URL(optional):");
            String url = scanner.nextLine();
            if (url.equals(":cancel")){
                break;
            }
            try{
                EncryptedPassword encrypted = encryptionService.encrypt(password, authService.getCurrentKey());
                boolean success = vaultRepository.addEntry(authService.getCurrentUserId(), serviceName, login, url, encrypted.getEncryptedPassword(), encrypted.getNonce());
                if (success) {
                    System.out.println("Saved succesfully");
                } else {
                    System.out.println("Not saved. Try again.");
                }
            }
            catch (IllegalStateException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void deleteEntryScreen(Scanner scanner){
        System.out.println("Selected option: 3");
        System.out.println("=".repeat(32));
        boolean i = displayEntries(authService.getCurrentUserId());
        if (!i){
            return;
        }
        while (true) {
            System.out.println("Enter entry ID to delete(empty to quit): ");
            String deleteId = scanner.nextLine().trim();
            if (deleteId.isBlank()){
                break;
            }
            try {
                boolean success = vaultRepository.deleteEntry(authService.getCurrentUserId(), Integer.parseInt(deleteId));
                if (success) {
                    System.out.println("Deleted succesfully");
                } else {
                    System.out.println("Entry not found. Try again.");
                }
            }
            catch (IllegalStateException e){
                System.out.println(e.getMessage());
            }
            catch (NumberFormatException e){
                System.out.println("Invalid input. Try again.");
            }
        }
    }

    public void generatePassword_screen(Scanner scanner) {
        System.out.println("Selected option: 3");
        System.out.println("=".repeat(32));
        System.out.println("Generate Password");
        System.out.println("=".repeat(32));

        boolean again = false;
        while (!again){
            System.out.println("\n 1. Generate random password.\n 2. Generate random Passphrase.\n 3. Return to main menu.\n");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            try {
                switch (Integer.parseInt(choice)){
                    case 1 : String pass = Generator.generateRandomPassword(Generator.CharacterTypes.ALPHANUMERIC, 15);System.out.println(pass);break;
                    case 2 : String pass1 = Generator.generatePassphrase("-", 5);System.out.println(pass1); break;
                    case 3: again = true; break;
                    default: System.out.println("Invalid choice."); break;
                }
            }
            catch (NumberFormatException e){
                System.out.println("Invalid input. Try again.");
            }

        }
    }

    public void terminate() {
        System.out.println("Selected option: 4");
        System.out.println("=".repeat(32));
        System.out.println("Exit");
        System.out.println("=".repeat(32));
        System.out.println("\nThank you for using SecureVault!\nGoodbye.\n");
    }
}
