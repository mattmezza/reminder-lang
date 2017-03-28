package me.matteomerola.remindev.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {
  public static enum TokenType {
    // Token types cannot have underscores
    NUMBER("[0-9]+"),
    STRING("\".*?\""),
    ON("on"),
    NEXT("next"),
    REPEAT("repeat"),
    EVERY("every"),
    UNTIL("until"),
    IN("in"),
    AT("at"),
    TODAY("today"),
    TONIGHT("tonight"),
    TOMORROW("tomorrow"),
    DATESEP("/"),
    HOUR("hour[s]?"),
    WEEK("week[s]?"),
    MONTH("month[s]?"),
    DAY("day[s]?"),
    YEAR("year[s]?"),
    TIMESEP(":"),
    TEXT("[a-zA-Z0-9_]+"),
    WHITESPACE("[ \t\f\r\n]+");

    public final String pattern;

    private TokenType(String pattern) {
      this.pattern = pattern;
    }
  }

  public static class Token {
    public TokenType type;
    public String data;
    public int pos;

    public Token(TokenType type, String data, int pos) {
      this.type = type;
      this.data = data;
      this.pos = pos;
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
      if (matcher.group(TokenType.ON.name()) != null) {
        tokens.add(new Token(TokenType.ON, matcher.group(TokenType.ON.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.EVERY.name()) != null) {
        tokens.add(new Token(TokenType.EVERY, matcher.group(TokenType.EVERY.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.NEXT.name()) != null) {
        tokens.add(new Token(TokenType.NEXT, matcher.group(TokenType.NEXT.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.REPEAT.name()) != null) {
        tokens.add(new Token(TokenType.REPEAT, matcher.group(TokenType.REPEAT.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.UNTIL.name()) != null) {
        tokens.add(new Token(TokenType.UNTIL, matcher.group(TokenType.UNTIL.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.WEEK.name()) != null) {
        tokens.add(new Token(TokenType.WEEK, matcher.group(TokenType.WEEK.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.HOUR.name()) != null) {
        tokens.add(new Token(TokenType.HOUR, matcher.group(TokenType.HOUR.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.MONTH.name()) != null) {
        tokens.add(new Token(TokenType.MONTH, matcher.group(TokenType.MONTH.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.DAY.name()) != null) {
        tokens.add(new Token(TokenType.DAY, matcher.group(TokenType.DAY.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.YEAR.name()) != null) {
        tokens.add(new Token(TokenType.YEAR, matcher.group(TokenType.YEAR.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.IN.name()) != null) {
        tokens.add(new Token(TokenType.IN, matcher.group(TokenType.IN.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.AT.name()) != null) {
        tokens.add(new Token(TokenType.AT, matcher.group(TokenType.AT.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.TODAY.name()) != null) {
        tokens.add(new Token(TokenType.TODAY, matcher.group(TokenType.TODAY.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.TONIGHT.name()) != null) {
        tokens.add(new Token(TokenType.TONIGHT, matcher.group(TokenType.TONIGHT.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.TOMORROW.name()) != null) {
        tokens.add(new Token(TokenType.TOMORROW, matcher.group(TokenType.TOMORROW.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.DATESEP.name()) != null) {
        tokens.add(new Token(TokenType.DATESEP, matcher.group(TokenType.DATESEP.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.TIMESEP.name()) != null) {
        tokens.add(new Token(TokenType.TIMESEP, matcher.group(TokenType.TIMESEP.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.STRING.name()) != null) {
        tokens.add(new Token(TokenType.STRING, matcher.group(TokenType.STRING.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.TEXT.name()) != null) {
        tokens.add(new Token(TokenType.TEXT, matcher.group(TokenType.TEXT.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.NUMBER.name()) != null) {
        tokens.add(new Token(TokenType.NUMBER, matcher.group(TokenType.NUMBER.name()), matcher.start()));
        continue;
      } else if (matcher.group(TokenType.WHITESPACE.name()) != null)
        continue;
    }

    return tokens;
  }

}