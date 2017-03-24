package me.matteomerola.remindev;

import java.util.Scanner;
import java.util.List;
import me.matteomerola.remindev.ai.Lexer;

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
        Lexer lexer = new Lexer();
        System.out.print("> ");
        String query = scanner.next();
        while (!query.equalsIgnoreCase(App.QUIT)) {
            List<Lexer.Token> tokens = lexer.lex(query);
            for (Lexer.Token token : tokens) {
                System.out.println(token);
            }
            
            System.out.print("> ");
            query = scanner.next();
        }
        System.out.println("Bye bye...");
    }
}
