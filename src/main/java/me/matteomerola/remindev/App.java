package me.matteomerola.remindev;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static final String QUIT = "q";
    public static void main( String[] args )
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        String query = scanner.next();
        while (!query.equalsIgnoreCase(App.QUIT)) {
            // simple echo
            System.out.println(query);
            
            System.out.print("> ");
            query = scanner.next();
        }
        System.out.println("Bye bye...");
    }
}
