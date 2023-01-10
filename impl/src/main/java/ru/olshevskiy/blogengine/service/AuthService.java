package ru.olshevskiy.blogengine.service;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Service;
import ru.olshevskiy.blogengine.dto.ErrorDescription;
import ru.olshevskiy.blogengine.dto.request.LoginRq;
import ru.olshevskiy.blogengine.dto.request.RegistrationRq;
import ru.olshevskiy.blogengine.dto.response.LoginAndCheckRs;
import ru.olshevskiy.blogengine.dto.response.LogoutRs;
import ru.olshevskiy.blogengine.dto.response.RegistrationRs;
import ru.olshevskiy.blogengine.exception.ex.EmailDuplicateException;
import ru.olshevskiy.blogengine.exception.ex.IncorrectCredentialsException;
import ru.olshevskiy.blogengine.exception.ex.InvalidCaptchaCodeException;
import ru.olshevskiy.blogengine.exception.ex.MultiuserModeException;
import ru.olshevskiy.blogengine.mapper.UserUserDtoMapper;
import ru.olshevskiy.blogengine.model.CaptchaCode;
import ru.olshevskiy.blogengine.model.GlobalSetting;
import ru.olshevskiy.blogengine.model.User;
import ru.olshevskiy.blogengine.repository.CaptchaRepository;
import ru.olshevskiy.blogengine.repository.GlobalSettingRepository;
import ru.olshevskiy.blogengine.repository.PostRepository;
import ru.olshevskiy.blogengine.repository.UserRepository;
import ru.olshevskiy.blogengine.security.SecurityUtils;

/**
 * AuthService.
 *
 * @author Sergey Olshevskiy
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final CaptchaRepository captchaRepository;
  private final PostRepository postRepository;
  private final GlobalSettingRepository globalSettingRepository;

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final TokenBasedRememberMeServices rememberMeServices;
  private final UserUserDtoMapper userUserDtoMapper;

  /**
   * AuthService. Check authorization of the current user method.
   */
  public LoginAndCheckRs check() {
    log.info("Start request getCheckAuthorization");
    if (SecurityUtils.userIsNotAuthorized()) {
      log.info("User isn't authorized");
      throw new IncorrectCredentialsException("Finish request getCheckAuthorization");
    }
    org.springframework.security.core.userdetails.User currentUser =
            SecurityUtils.getCurrentSecurityUser();
    LoginAndCheckRs checkRs = getLoginAndCheckRs(currentUser.getUsername());
    log.info("Finish request getCheckAuthorization");
    return checkRs;
  }

  /**
   * AuthService. New user registration method.
   */
  public RegistrationRs register(RegistrationRq registrationRq) {
    log.info("Start request register");
    RegistrationRs registrationRs = new RegistrationRs();
    GlobalSetting globalSetting = globalSettingRepository.findByCode("MULTIUSER_MODE");
    checkRegistrationInfo(globalSetting, registrationRq);
    User newUser = createNewUser(registrationRq);
    userRepository.save(newUser);
    registrationRs.setResult(true);
    log.info("Finish request register");
    return registrationRs;
  }

  /**
   * AuthService. User login method.
   */
  public LoginAndCheckRs login(LoginRq loginRq, HttpServletRequest request,
                               HttpServletResponse response) {
    log.info("Start request login");
    LoginAndCheckRs loginRs;
    try {
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginRq.getEmail(), loginRq.getPassword()));
      rememberMeServices.onLoginSuccess(request, response, authentication);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      org.springframework.security.core.userdetails.User principal =
              (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
      loginRs = getLoginAndCheckRs(principal.getUsername());
    } catch (BadCredentialsException | UsernameNotFoundException e) {
      log.info(e.toString());
      throw new IncorrectCredentialsException("Finish request login");
    }
    log.info("Finish request login");
    return loginRs;
  }

  /**
   * AuthService. User logout method.
   */
  public LogoutRs logout(HttpServletRequest request, HttpServletResponse response) {
    log.info("Start request logout");
    LogoutRs logoutRs = new LogoutRs();
    logoutRs.setResult(true);
    if (SecurityUtils.userIsNotAuthorized()) {
      log.info("Finish request logout");
      return logoutRs;
    }
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      rememberMeServices.logout(request, response, authentication);
      SecurityContextHolder.clearContext();
    } catch (Exception e) {
      log.info("Logout error: " + e);
    }
    log.info("Finish request logout");
    return logoutRs;
  }

  private LoginAndCheckRs getLoginAndCheckRs(String email) {
    User currentUser = userRepository.findByEmail(email).orElseThrow(() ->
            new UsernameNotFoundException(String.format(
                    ErrorDescription.USER_NOT_FOUND, email)));
    return new LoginAndCheckRs().setResult(true)
            .setUser(userUserDtoMapper.userToUserDto(currentUser,
                    postRepository.getModerationPostsCount(currentUser.getId())));
  }

  private void checkRegistrationInfo(GlobalSetting globalSetting, RegistrationRq registrationRq) {
    if (globalSetting.getValue().equals("NO")) {
      throw new MultiuserModeException();
    }
    Optional<User> user = userRepository.findByEmail(registrationRq.getEmail());
    if (user.isPresent()) {
      throw new EmailDuplicateException();
    }
    Optional<CaptchaCode> captcha = captchaRepository.findByCode(registrationRq.getCaptcha());
    if (captcha.isEmpty() || !captcha.get().getSecretCode()
            .equals(registrationRq.getCaptchaSecret())) {
      throw new InvalidCaptchaCodeException();
    }
  }

  private User createNewUser(RegistrationRq registrationRq) {
    return new User()
            .setName(registrationRq.getName())
            .setEmail(registrationRq.getEmail())
            .setIsModerator((byte) 0)
            .setRegTime(LocalDateTime.now())
            .setPassword(passwordEncoder.encode(registrationRq.getPassword()));
  }
}