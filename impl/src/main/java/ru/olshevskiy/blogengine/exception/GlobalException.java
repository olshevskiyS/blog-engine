package ru.olshevskiy.blogengine.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestControllerAdvice
public class GlobalException {

  private static int minPasswordLength;
  private static int minTitleLength;
  private static int minTextLength;

  @Value("${blog.min-password-length}")
  public void setMinPasswordLength(int length) {
    minPasswordLength = length;
  }

  @Value("${blog.post.titleLength}")
  public void setMinTitleLength(int length) {
    minTitleLength = length;
  }

  @Value("${blog.post.textLength}")
  public void setMinTextLength(int length) {
    minTextLength = length;
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
  public ResponseEntity<IncorrectCredentialsDto> handleIncorrectCredentialsException(
         IncorrectCredentialsException ex) {
    IncorrectCredentialsDto result = new IncorrectCredentialsDto();
    result.setResult(false);
    log.info(ex.getMessage());
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
        case "title":
          errorsDescription.put("title",
                  String.format(ErrorDescription.POST_TITLE_TOO_SMALL, minTitleLength));
          break;
        case "text":
          errorsDescription.put("text",
                  String.format(ErrorDescription.POST_TEXT_TOO_SMALL, minTextLength));
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