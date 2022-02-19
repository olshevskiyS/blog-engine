package ru.olshevskiy.blogengine.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.olshevskiy.blogengine.service.UserServiceImpl;

/**
 * Контроллер API для запросов авторизации пользователей.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiAuthController {

  private final UserServiceImpl userService;

  @GetMapping("/auth/check")
  private ResponseEntity<Map<String, Object>> checkUserAuthorization() {
    return new ResponseEntity<>(userService.getCheckAuthorization(), HttpStatus.UNAUTHORIZED);
  }
}
