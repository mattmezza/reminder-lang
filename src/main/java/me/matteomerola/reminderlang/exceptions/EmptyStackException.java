package me.matteomerola.reminderlang.exceptions;

public class EmptyStackException extends Exception {

  public static final long serialVersionUID = 9876;

  public EmptyStackException() {
    super("The token stack is empty.");
  }

}