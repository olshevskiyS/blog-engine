package ru.olshevskiy.blogengine.resource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.olshevskiy.blogengine.dto.request.ChangePassRq;
import ru.olshevskiy.blogengine.dto.request.LoginRq;
import ru.olshevskiy.blogengine.dto.request.RegistrationRq;
import ru.olshevskiy.blogengine.dto.request.RestorePassRq;
import ru.olshevskiy.blogengine.dto.response.CaptchaRs;
import ru.olshevskiy.blogengine.dto.response.ChangePassRs;
import ru.olshevskiy.blogengine.dto.response.LoginAndCheckRs;
import ru.olshevskiy.blogengine.dto.response.LogoutRs;
import ru.olshevskiy.blogengine.dto.response.RegistrationRs;
import ru.olshevskiy.blogengine.dto.response.RestorePassRs;
import ru.olshevskiy.blogengine.service.AuthService;
import ru.olshevskiy.blogengine.service.CaptchaService;

/**
 * AuthResource.
 *
 * @author Sergey Olshevskiy
 */
@RestController
@RequiredArgsConstructor
public class AuthResource implements ApiAuthController {

  private final AuthService authService;
  private final CaptchaService captchaService;

  @Override
  public ResponseEntity<LoginAndCheckRs> check() {
    return new ResponseEntity<>(authService.check(), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<RegistrationRs> register(RegistrationRq registrationRq) {
    return new ResponseEntity<>(authService.register(registrationRq), HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<CaptchaRs> getCaptcha() {
    return new ResponseEntity<>(captchaService.getCaptcha(), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<LoginAndCheckRs> login(LoginRq loginRq,
                                               HttpServletRequest request,
                                               HttpServletResponse response) {
    return new ResponseEntity<>(authService.login(loginRq, request, response), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<LogoutRs> logout(HttpServletRequest request, HttpServletResponse response) {
    return new ResponseEntity<>(authService.logout(request, response), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<RestorePassRs> restorePassword(RestorePassRq restorePassRq) {
    return new ResponseEntity<>(authService.restorePassword(restorePassRq), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ChangePassRs> changePassword(ChangePassRq changePassRq) {
    return new ResponseEntity<>(authService.changePassword(changePassRq), HttpStatus.OK);
  }
}