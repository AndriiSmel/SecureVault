package com.securevault;
import java.util.Scanner;
/**
 *
 * mvn compile && java -cp target/classes com.securevault.App
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Scanner input = new Scanner(System.in);
        System.out.println("=".repeat(32));
        System.out.println( "Welcome to SECUREVAULT \n v1.0 by Andrii" );
        System.out.println( "A local, encrypted password manager." );
        System.out.println("=".repeat(32));

        System.out.println("Username:");
        String Username = input.nextLine();
        System.out.println("Password:");
        String Password = input.nextLine();

        System.out.println("Username: " + Username);
    }
}
