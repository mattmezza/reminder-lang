package me.matteomerola.remindev;

import java.util.Scanner;
import java.util.List;
import me.matteomerola.remindev.ai.Lexer;
import me.matteomerola.remindev.ai.Parser;
import me.matteomerola.remindev.ai.exceptions.ParseException;
import me.matteomerola.remindev.ai.model.Reminder;

/**
 * Hello world!
 *
 */
public class App {
    public static final String QUIT = "q";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        String query = scanner.nextLine();
        while (!query.equalsIgnoreCase(App.QUIT)) {
            List<Lexer.Token> tokens = Lexer.lex(query);
            // System.out.println("List of tokens");
            // for (Lexer.Token token : tokens) {
                // System.out.println("token: " + token);
            // }

            try {
                Parser parser = new Parser(query, tokens);
                Reminder r = parser.parse();
                System.out.println(r.toString());
            } catch (ParseException e) {
                System.err.println(e.getMessage());
            }

            System.out.print("> ");
            query = scanner.nextLine();
        }
        System.out.println("Bye bye...");
        scanner.close();
    }
}
