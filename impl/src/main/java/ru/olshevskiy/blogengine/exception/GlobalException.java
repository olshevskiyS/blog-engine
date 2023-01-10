package ru.olshevskiy.blogengine.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.olshevskiy.blogengine.dto.Error;
import ru.olshevskiy.blogengine.dto.ErrorDescription;
import ru.olshevskiy.blogengine.dto.IncorrectCredentialsDto;
import ru.olshevskiy.blogengine.dto.response.PostByIdRs;
import ru.olshevskiy.blogengine.dto.response.RegistrationRs;
import ru.olshevskiy.blogengine.exception.ex.EmailDuplicateException;
import ru.olshevskiy.blogengine.exception.ex.IncorrectCredentialsException;
import ru.olshevskiy.blogengine.exception.ex.InvalidCaptchaCodeException;
import ru.olshevskiy.blogengine.exception.ex.MultiuserModeException;
import ru.olshevskiy.blogengine.exception.ex.PostNotFoundException;

/**
 * GlobalException.
 *
 * @author Sergey Olshevskiy
 */
@RestControllerAdvice
public class GlobalException {

  private static int minPasswordLength;

  @Value("${blog.min-password-length}")
  public void setMinPasswordLength(int length) {
    minPasswordLength = length;
  }

  @ExceptionHandler(PostNotFoundException.class)
  public ResponseEntity<PostByIdRs> handlePostNotFoundException() {
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MultiuserModeException.class)
  public ResponseEntity<RegistrationRs> handleMultiuserModeException() {
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  /**
   * IncorrectCredentialsException handler.
   */
  @ExceptionHandler(IncorrectCredentialsException.class)
  public ResponseEntity<IncorrectCredentialsDto> handleIncorrectCredentialsException() {
    IncorrectCredentialsDto result = new IncorrectCredentialsDto();
    result.setResult(false);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * EmailDuplicateException handler.
   */
  @ExceptionHandler(EmailDuplicateException.class)
  public ResponseEntity<Error> handleEmailDuplicateException() {
    Map<String, String> errorsDescription = new HashMap<>();
    errorsDescription.put("email", ErrorDescription.EMAIL_DUPLICATE);
    Error error = new Error()
            .setResult(false)
            .setErrors(errorsDescription);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * InvalidCaptchaCodeException handler.
   */
  @ExceptionHandler(InvalidCaptchaCodeException.class)
  public ResponseEntity<Error> handleInvalidCaptchaCodeException() {
    Map<String, String> errorsDescription = new HashMap<>();
    errorsDescription.put("captcha", ErrorDescription.INVALID_CAPTCHA_CODE);
    Error error = new Error()
            .setResult(false)
            .setErrors(errorsDescription);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * MethodArgumentNotValidException handler.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Error> handleMethodArgumentNotValidException(
          MethodArgumentNotValidException ex) {
    Error error = new Error().setResult(false);
    Map<String, String> errorsDescription = new HashMap<>();
    BindingResult bindingResult = ex.getBindingResult();
    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
    for (FieldError fieldError : fieldErrors) {
      switch (fieldError.getField()) {
        case "email":
          errorsDescription.put("email", ErrorDescription.INCORRECT_EMAIL_FORMAT);
          break;
        case "password":
          errorsDescription.put("password",
                  String.format(ErrorDescription.PASSWORD_LENGTH_TOO_SMALL, minPasswordLength));
          break;
        default: {
          errorsDescription.put("name", ErrorDescription.NAME_LENGTH_TOO_SMALL);
        }
      }
    }
    error.setErrors(errorsDescription);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }
}
