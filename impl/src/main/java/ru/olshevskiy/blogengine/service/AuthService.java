package ru.olshevskiy.blogengine.service;

import java.net.InetAddress;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import ru.olshevskiy.blogengine.dto.request.ChangePassRq;
import ru.olshevskiy.blogengine.dto.request.LoginRq;
import ru.olshevskiy.blogengine.dto.request.RegistrationRq;
import ru.olshevskiy.blogengine.dto.request.RestorePassRq;
import ru.olshevskiy.blogengine.dto.response.ChangePassRs;
import ru.olshevskiy.blogengine.dto.response.LoginAndCheckRs;
import ru.olshevskiy.blogengine.dto.response.LogoutRs;
import ru.olshevskiy.blogengine.dto.response.RegistrationRs;
import ru.olshevskiy.blogengine.dto.response.RestorePassRs;
import ru.olshevskiy.blogengine.exception.ex.EmailDuplicateException;
import ru.olshevskiy.blogengine.exception.ex.IncorrectCredentialsException;
import ru.olshevskiy.blogengine.exception.ex.InvalidCaptchaCodeException;
import ru.olshevskiy.blogengine.exception.ex.InvalidVerificationCodeException;
import ru.olshevskiy.blogengine.exception.ex.MultiuserModeException;
import ru.olshevskiy.blogengine.exception.ex.VerificationCodeOutdatedException;
import ru.olshevskiy.blogengine.exception.ex.WrongParamInputException;
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
  private final EmailService emailService;

  @Value("${server.port}")
  private String serverPort;
  @Value("${blog.password-restore.message-subject}")
  private String messageSubject;
  @Value("${blog.password-restore.link-pattern-restore}")
  private String linkPatternRestorePassword;
  @Value("${blog.password-restore.message-text}")
  private String messageText;
  @Value("${blog.password-restore.verification-code-time}")
  private Long verificationCodeTime;
  @Value("${blog.password-restore.link-pattern-repeat}")
  private String linkPatternRepeatRestorePassword;

  /**
   * AuthService. New user registration method.
   */
  public RegistrationRs register(RegistrationRq registrationRq) {
    log.info("Start request register");
    GlobalSetting globalSetting = globalSettingRepository.findByCode("MULTIUSER_MODE");
    checkIsRegistrationAllowedAndIsNotEmailDuplicated(globalSetting, registrationRq.getEmail());
    checkCaptchaForRequest("register",
            registrationRq.getCaptcha(), registrationRq.getCaptchaSecret());
    User newUser = createNewUser(registrationRq);
    userRepository.save(newUser);
    log.info("Finish request register");
    return new RegistrationRs().setResult(true);
  }

  private void checkIsRegistrationAllowedAndIsNotEmailDuplicated(GlobalSetting globalSetting,
                                                                 String email) {
    if (globalSetting.getValue().equals("NO")) {
      log.info("Registration is not allowed. Finish request register with exception");
      throw new MultiuserModeException();
    }
    Optional<User> user = userRepository.findByEmail(email);
    if (user.isPresent()) {
      log.info("Email is already registered. Finish request register with exception");
      throw new EmailDuplicateException();
    }
  }

  private User createNewUser(RegistrationRq registrationRq) {
    return new User(registrationRq.getName(), registrationRq.getEmail())
            .setPassword(passwordEncoder.encode(registrationRq.getPassword()));
  }

  /**
   * AuthService. Check authorization of the current user method.
   */
  public LoginAndCheckRs check() {
    log.info("Start request checkAuthorization");
    if (SecurityUtils.userIsNotAuthorized()) {
      log.info("User isn't authorized");
      throw new IncorrectCredentialsException("Finish request checkAuthorization");
    }
    org.springframework.security.core.userdetails.User currentUser =
            (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
    LoginAndCheckRs checkRs = getLoginAndCheckRs(currentUser.getUsername());
    log.info("Finish request checkAuthorization");
    return checkRs;
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

  private LoginAndCheckRs getLoginAndCheckRs(String email) {
    User currentUser = userRepository.findByEmail(email).orElseThrow(() ->
            new UsernameNotFoundException(String.format(
                    ErrorDescription.USER_NOT_FOUND, email)));
    return new LoginAndCheckRs().setResult(true)
            .setUser(userUserDtoMapper.userToUserDto(currentUser,
                    postRepository.getModerationPostsCount(currentUser.getId())));
  }

  /**
   * AuthService. User logout method.
   */
  public LogoutRs logout(HttpServletRequest request, HttpServletResponse response) {
    log.info("Start request logout");
    LogoutRs logoutRs = new LogoutRs();
    logoutRs.setResult(true);
    if (SecurityUtils.userIsNotAuthorized()) {
      log.info("User isn't authorized. Finish request logout with exception");
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

  /**
   * AuthService. Restore password method.
   */
  public RestorePassRs restorePassword(RestorePassRq restorePassRq) {
    log.info("Start request restorePassword, email - " + restorePassRq.getEmail());
    checkCorrectnessEmail(restorePassRq.getEmail());
    Optional<User> currentUserOptional = userRepository.findByEmail(restorePassRq.getEmail());
    if (currentUserOptional.isEmpty()) {
      log.info("User not found. Finish request restorePassword with exception");
      return new RestorePassRs().setResult(false);
    }
    User currentUser = currentUserOptional.get();
    String verificationCode = UUID.randomUUID().toString();
    currentUser.setCode(verificationCode)
               .setCodeCreateTime(LocalDateTime.now(ZoneId.ofOffset("UTC", ZoneOffset.UTC)));
    userRepository.save(currentUser);
    formAndSendMessage(verificationCode, currentUser);
    log.info("Finish request restorePassword");
    return new RestorePassRs().setResult(true);
  }

  private void checkCorrectnessEmail(String email) {
    String pattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    if (!email.matches(pattern)) {
      log.info("Incorrect email format. Finish request restorePassword with exception");
      throw new WrongParamInputException(ErrorDescription.INCORRECT_EMAIL_FORMAT);
    }
  }

  private void formAndSendMessage(String verificationCode, User currentUser) {
    String link = String.format(linkPatternRestorePassword,
            InetAddress.getLoopbackAddress().getHostName(), serverPort, verificationCode);
    String messageTextWithLink = String.format(messageText, currentUser.getName(), link);
    emailService.sendMessage(currentUser.getEmail(), messageSubject, messageTextWithLink);
  }

  /**
   * AuthService. Change password method.
   */
  public ChangePassRs changePassword(ChangePassRq changePassRq) {
    log.info("Start request changePassword");
    checkCaptchaForRequest("changePassword",
            changePassRq.getCaptcha(), changePassRq.getCaptchaSecret());
    Optional<User> optionalCurrentUser = userRepository.findByCode(changePassRq.getCode());
    User currentUser = checkReliabilityVerificationCodeAndGetCurrentUser(optionalCurrentUser);
    currentUser.setPassword(passwordEncoder.encode(changePassRq.getPassword()))
               .setCode(null)
               .setCodeCreateTime(null);
    userRepository.save(currentUser);
    log.info("Finish request changePassword, user id " + currentUser.getId());
    return new ChangePassRs().setResult(true);
  }

  private User checkReliabilityVerificationCodeAndGetCurrentUser(
          Optional<User> optionalCurrentUser) {
    if (optionalCurrentUser.isEmpty()) {
      log.info("Invalid verification code. Finish request changePassword with exception");
      throw new InvalidVerificationCodeException();
    }
    if (verificationCodeIsOutdated(optionalCurrentUser.get().getCodeCreateTime())) {
      String linkToRepeatRequest = String.format(linkPatternRepeatRestorePassword,
              InetAddress.getLoopbackAddress().getHostName(), serverPort);
      log.info("Verification code is outdated. Finish request changePassword with exception");
      throw new VerificationCodeOutdatedException(linkToRepeatRequest);
    }
    return optionalCurrentUser.get();
  }

  private boolean verificationCodeIsOutdated(LocalDateTime codeCreateTime) {
    return (codeCreateTime.toEpochSecond(ZoneOffset.UTC) + verificationCodeTime)
            < Instant.now().getEpochSecond();
  }

  private void checkCaptchaForRequest(String requestName,
                                      String captchaCode, String captchaSecret) {
    Optional<CaptchaCode> captcha = captchaRepository.findByCode(captchaCode);
    if (captcha.isEmpty() || !captcha.get().getSecretCode().equals(captchaSecret)) {
      log.info("Invalid captcha code. Finish request " + requestName + " with exception");
      throw new InvalidCaptchaCodeException();
    }
  }
}