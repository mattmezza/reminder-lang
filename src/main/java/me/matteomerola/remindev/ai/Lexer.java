package me.matteomerola.remindev.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {
  public static enum TokenType {
    // Token types cannot have underscores
    NUMBER("[1-9][0-9]*"),
    REMINDME("remind-me"),
    QUOTE("\""),
    TEXT("[a-zA-Z0-9_]+"),
    ON("on"),
    DATESEP("/"),
    TIMESEP(":"),
    WHITESPACE("[ \t\f\r\n]+");

    public final String pattern;

    private TokenType(String pattern) {
      this.pattern = pattern;
    }
  }

  public static class Token {
    public TokenType type;
    public String data;

    public Token(TokenType type, String data) {
      this.type = type;
      this.data = data;
    }

    @Override
    public String toString() {
      return String.format("(%s %s)", type.name(), data);
    }
  }

  public static List<Token> lex(String input) {
    // The tokens to return
    List<Token> tokens = new ArrayList<Token>();

    // Lexer logic begins here
    StringBuffer tokenPatternsBuffer = new StringBuffer();
    for (TokenType tokenType : TokenType.values()) {
      tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
    }
    Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));

    // Begin matching tokens
    Matcher matcher = tokenPatterns.matcher(input);
    while (matcher.find()) {
      if (matcher.group(TokenType.REMINDME.name()) != null) {
        tokens.add(new Token(TokenType.REMINDME, matcher.group(TokenType.REMINDME.name())));
        continue;
      } else if (matcher.group(TokenType.ON.name()) != null) {
        tokens.add(new Token(TokenType.ON, matcher.group(TokenType.ON.name())));
        continue;
      } else if (matcher.group(TokenType.DATESEP.name()) != null) {
        tokens.add(new Token(TokenType.DATESEP, matcher.group(TokenType.DATESEP.name())));
        continue;
      } else if (matcher.group(TokenType.TIMESEP.name()) != null) {
        tokens.add(new Token(TokenType.TIMESEP, matcher.group(TokenType.TIMESEP.name())));
        continue;
      } else if (matcher.group(TokenType.QUOTE.name()) != null) {
        tokens.add(new Token(TokenType.QUOTE, matcher.group(TokenType.QUOTE.name())));
        continue;
      } else if (matcher.group(TokenType.TEXT.name()) != null) {
        tokens.add(new Token(TokenType.TEXT, matcher.group(TokenType.TEXT.name())));
        continue;
      } else if (matcher.group(TokenType.NUMBER.name()) != null) {
        tokens.add(new Token(TokenType.NUMBER, matcher.group(TokenType.NUMBER.name())));
        continue;
      } else if (matcher.group(TokenType.WHITESPACE.name()) != null)
        continue;
    }

    return tokens;
  }

}