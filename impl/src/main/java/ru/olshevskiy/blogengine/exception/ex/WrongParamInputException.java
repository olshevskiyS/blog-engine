package ru.olshevskiy.blogengine.exception.ex;

/**
 * WrongParamInputException.
 *
 * @author Sergey Olshevskiy
 */
public class WrongParamInputException extends RuntimeException {

  public WrongParamInputException(String message) {
    super(message);
  }
}
