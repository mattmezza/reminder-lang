package me.matteomerola.reminderlang.exceptions;

import me.matteomerola.reminderlang.Lexer.Token;

public class ParseException extends Exception {
  static final long serialVersionUID = 99999999;
  private String input;
  private Token token;
  private String message;

  public ParseException(String input, Token token, String message) {
    super();
    this.input = input;
    this.token = token;
    this.message = message;
  }

  public ParseException(String input, Token token, String message, Throwable cause) {
    super(cause);
    this.input = input;
    this.token = token;
    this.message = message;
  }

  @Override
  public String getMessage() {
    String cause = "";
    if (this.getCause() != null) {
      cause = "\nCaused by: " + this.getCause().getMessage();
    }
    if (this.token == null) {
      return "ParseException: " + this.message + "\n" + this.input + "[]" + cause;
    }
    String erroredInput = this.input.substring(0, this.token.pos);
    erroredInput += "[";
    erroredInput += this.token.data;
    erroredInput += "]";
    erroredInput += this.input.substring(this.token.pos + this.token.data.length());
    return "ParseException: " + this.message + "\n" + erroredInput + cause;
  }
}