package com.securevault.ui;
import com.securevault.service.AuthService;
import java.util.Scanner;

public class MainMenu {
    private final AuthService authService;

    public MainMenu(AuthService authService) {
        this.authService = authService;
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
        System.out.println("Username: \n");
        String username = scanner.nextLine();
        System.out.println("Password: \n");
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
                break;
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
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    public void generatePassword_screen(Scanner scanner) {
        System.out.println("Selected option: 3");
        System.out.println("=".repeat(32));
        System.out.println("Generate Password");
        System.out.println("=".repeat(32));

    //        System.out.println("\n 1. Generate random password.\n 2. Generate random Passphrase.\n 3. Return to main menu.\n");
    //        System.out.print("Enter your choice: ");
    //        String choice = scanner.nextLine();
    //        switch (Integer.parseInt(choice)){
    //            case 1 : String pass1 = Generator.generatePassphrase("-", 5);
    //            case 2 : String pass = Generator.generateRandomPassword(Generator.CharacterTypes.ALPHANUMERIC, 15);
    //            case 3: mainMenu(scanner);
    //            default: System.out.println("Invalid choice."); break;
    //        }
    //
    //        scanner.nextLine();
    }

    public void terminate() {
        System.out.println("Selected option: 4");
        System.out.println("=".repeat(32));
        System.out.println("Exit");
        System.out.println("=".repeat(32));
        System.out.println("\nThank you for using SecureVault!\nGoodbye.\n");
    }
}
