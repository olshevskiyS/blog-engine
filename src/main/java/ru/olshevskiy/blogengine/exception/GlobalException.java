package ru.olshevskiy.blogengine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.olshevskiy.blogengine.exception.ex.PostNotFoundException;
import ru.olshevskiy.blogengine.model.dto.PostByIdDto;

/**
 * ControllerAdvice.
 *
 * @author Sergey Olshevskiy
 */
@RestControllerAdvice
public class GlobalException {

  @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<PostByIdDto> handlePostNotFoundException(PostNotFoundException ex) {
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}
