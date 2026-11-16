package com.securevault;
import com.securevault.repository.UserRepository;
import com.securevault.service.AuthService;
import com.securevault.ui.MainMenu;
import java.util.Scanner;


public class App 
{
    public static void main( String[] args ){
        Scanner scanner = new Scanner(System.in);
        System.out.println("=".repeat(32));
        System.out.println( "Welcome to SECUREVAULT \n v1.0 by Andrii" );
        System.out.println( "A local, encrypted password manager." );
        System.out.println("=".repeat(32));
        UserRepository userRepository = new UserRepository();
        AuthService authService = new AuthService(userRepository);
        MainMenu menu = new MainMenu(authService);
        menu.mainMenu(scanner);
    }
}
