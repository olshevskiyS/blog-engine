package ru.olshevskiy.blogengine.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
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
import ru.olshevskiy.blogengine.dto.InvalidInput;
import ru.olshevskiy.blogengine.dto.response.RegistrationRs;
import ru.olshevskiy.blogengine.exception.ex.EmailDuplicateException;
import ru.olshevskiy.blogengine.exception.ex.IncorrectCredentialsException;
import ru.olshevskiy.blogengine.exception.ex.InvalidCaptchaCodeException;
import ru.olshevskiy.blogengine.exception.ex.InvalidCommentException;
import ru.olshevskiy.blogengine.exception.ex.InvalidImageExtensionException;
import ru.olshevskiy.blogengine.exception.ex.InvalidVerificationCodeException;
import ru.olshevskiy.blogengine.exception.ex.MultiuserModeException;
import ru.olshevskiy.blogengine.exception.ex.VerificationCodeOutdatedException;
import ru.olshevskiy.blogengine.exception.ex.WrongParamInputException;

/**
 * GlobalException.
 *
 * @author Sergey Olshevskiy
 */
@Slf4j
@RestControllerAdvice
public class GlobalException {

  @Value("${blog.min-password-length}")
  private int minPasswordLength;

  @Value("${blog.post.titleLength}")
  private int minTitleLength;

  @Value("${blog.post.textLength}")
  private int minTextLength;

  @Value("${spring.servlet.multipart.max-file-size}")
  private String maxImageSize;

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
    Error error = formErrorInfo("email", ErrorDescription.EMAIL_DUPLICATE);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * InvalidCaptchaCodeException handler.
   */
  @ExceptionHandler(InvalidCaptchaCodeException.class)
  public ResponseEntity<Error> handleInvalidCaptchaCodeException() {
    Error error = formErrorInfo("captcha", ErrorDescription.INVALID_CAPTCHA_CODE);
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

  /**
   * FileSizeLimitExceededException handler.
   */
  @ExceptionHandler(SizeLimitExceededException.class)
  public ResponseEntity<Error> handleFileSizeLimitExceededException() {
    Error error = formErrorInfo("photo",
            String.format(ErrorDescription.IMAGE_SIZE_TOO_LARGE, maxImageSize));
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * InvalidImageExtensionException handler.
   */
  @ExceptionHandler(InvalidImageExtensionException.class)
  public ResponseEntity<Error> handleInvalidImageExtensionException() {
    Error error = formErrorInfo("photo", ErrorDescription.INVALID_IMAGE_EXTENSION);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * WrongParamInputException handler.
   */
  @ExceptionHandler(WrongParamInputException.class)
  public ResponseEntity<InvalidInput> handleWrongParamInputException(WrongParamInputException ex) {
    InvalidInput invalidInput = new InvalidInput();
    invalidInput.setMessage(ex.getMessage());
    return new ResponseEntity<>(invalidInput, HttpStatus.BAD_REQUEST);
  }

  /**
   * InvalidCommentException handler.
   */
  @ExceptionHandler(InvalidCommentException.class)
  public ResponseEntity<Error> handleInvalidCommentException() {
    Error error = formErrorInfo("text", ErrorDescription.COMMENT_TEXT_TOO_SMALL);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * InvalidVerificationCodeException handler.
   */
  @ExceptionHandler(InvalidVerificationCodeException.class)
  public ResponseEntity<Error> handleInvalidVerificationCodeException() {
    Error error = formErrorInfo("code", ErrorDescription.INVALID_VERIFICATION_CODE);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * VerificationCodeOutdatedException handler.
   */
  @ExceptionHandler(VerificationCodeOutdatedException.class)
  public ResponseEntity<Error> handleVerificationCodeOutdatedException(
          VerificationCodeOutdatedException ex) {
    Error error = formErrorInfo("code",
            String.format(ErrorDescription.VERIFICATION_CODE_OUTDATED, ex.getMessage()));
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  private Error formErrorInfo(String errorSubject, String errorDescription) {
    Error error = new Error().setResult(false);
    Map<String, String> errorInfo = new HashMap<>();
    errorInfo.put(errorSubject, errorDescription);
    error.setErrors(errorInfo);
    return error;
  }
}