package ru.olshevskiy.blogengine.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.olshevskiy.blogengine.model.dto.request.LoginRq;
import ru.olshevskiy.blogengine.model.dto.request.RegistrationRq;
import ru.olshevskiy.blogengine.model.dto.response.CaptchaRs;
import ru.olshevskiy.blogengine.model.dto.response.LoginAndCheckRs;
import ru.olshevskiy.blogengine.model.dto.response.LogoutRs;
import ru.olshevskiy.blogengine.model.dto.response.RegistrationRs;
import ru.olshevskiy.blogengine.service.AuthServiceImpl;
import ru.olshevskiy.blogengine.service.CaptchaServiceImpl;

/**
 * ApiAuthController.
 *
 * @author Sergey Olshevskiy
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

  private final AuthServiceImpl authService;
  private final CaptchaServiceImpl captchaService;

  @GetMapping("/check")
  public ResponseEntity<LoginAndCheckRs> check() {
    return new ResponseEntity<>(authService.check(), HttpStatus.OK);
  }

  @PostMapping("/register")
  public ResponseEntity<RegistrationRs> register(
          @Valid @RequestBody RegistrationRq registrationRq) {
    return new ResponseEntity<>(authService.register(registrationRq), HttpStatus.CREATED);
  }

  @GetMapping("/captcha")
  public ResponseEntity<CaptchaRs> getCaptcha() {
    return new ResponseEntity<>(captchaService.getCaptcha(), HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginAndCheckRs> login(@RequestBody LoginRq loginRq,
                                               HttpServletRequest request,
                                               HttpServletResponse response) {
    return new ResponseEntity<>(authService.login(loginRq, request, response), HttpStatus.OK);
  }

  @GetMapping("/logout")
  public ResponseEntity<LogoutRs> logout(HttpServletRequest request, HttpServletResponse response) {
    return new ResponseEntity<>(authService.logout(request, response), HttpStatus.OK);
  }
}
