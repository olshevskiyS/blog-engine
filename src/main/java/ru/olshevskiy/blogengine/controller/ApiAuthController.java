package ru.olshevskiy.blogengine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.olshevskiy.blogengine.model.dto.CheckAuthorizationDto;
import ru.olshevskiy.blogengine.service.UserServiceImpl;

/**
 * ApiAuthController.
 *
 * @author Sergey Olshevskiy
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiAuthController {

  private final UserServiceImpl userResource;

  @GetMapping("/auth/check")
  private ResponseEntity<CheckAuthorizationDto> checkUserAuthorization() {
    return new ResponseEntity<>(userResource.getCheckAuthorization(), HttpStatus.UNAUTHORIZED);
  }
}
