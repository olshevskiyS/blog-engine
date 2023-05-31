package ru.olshevskiy.blogengine.exception.ex;

/**
 * VerificationCodeOutdatedException.
 *
 * @author Sergey Olshevskiy
 */
public class VerificationCodeOutdatedException extends RuntimeException {

  public VerificationCodeOutdatedException(String message) {
    super(message);
  }
}