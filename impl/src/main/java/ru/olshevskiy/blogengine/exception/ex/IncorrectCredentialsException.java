package ru.olshevskiy.blogengine.exception.ex;

/**
 * IncorrectCredentialsException.
 *
 * @author Sergey Olshevskiy
 */
public class IncorrectCredentialsException extends RuntimeException {

  public IncorrectCredentialsException(String message) {
    super(message);
  }
}
