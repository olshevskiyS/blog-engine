package ru.olshevskiy.blogengine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import ru.olshevskiy.blogengine.dto.request.ChangePassRq;
import ru.olshevskiy.blogengine.dto.response.ChangePassRs;
import ru.olshevskiy.blogengine.exception.ex.InvalidCaptchaCodeException;
import ru.olshevskiy.blogengine.exception.ex.InvalidVerificationCodeException;
import ru.olshevskiy.blogengine.exception.ex.VerificationCodeOutdatedException;
import ru.olshevskiy.blogengine.model.CaptchaCode;
import ru.olshevskiy.blogengine.model.User;
import ru.olshevskiy.blogengine.repository.CaptchaRepository;
import ru.olshevskiy.blogengine.repository.UserRepository;
import ru.olshevskiy.blogengine.service.AuthService;

/**
 * PasswordChangeRequestUnitTest.
 *
 * @author Sergey Olshevskiy
 */
@ExtendWith(MockitoExtension.class)
public class PasswordChangeRequestUnitTest {

  @InjectMocks
  private AuthService authService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private CaptchaRepository captchaRepository;
  @Mock
  private PasswordEncoder passwordEncoderMock;

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  private final ChangePassRq request = new ChangePassRq();
  private final CaptchaCode testCaptcha = new CaptchaCode();
  private final User testUser = new User();

  @BeforeEach
  void setup() {
    request.setCode("code")
           .setPassword("1111111")
           .setCaptcha("captcha")
           .setCaptchaSecret("secret");

    testCaptcha.setSecretCode("secret");
  }

  void initSourceData1() {
    when(captchaRepository.findByCode(request.getCaptcha())).thenReturn(Optional.of(testCaptcha));
    ReflectionTestUtils.setField(authService, "verificationCodeTime", 600L);
    testUser.setCode("code")
            .setCodeCreateTime(LocalDateTime.now())
            .setPassword("oldPassword");
    when(userRepository.findByCode(request.getCode())).thenReturn(Optional.of(testUser));
    when(passwordEncoderMock.encode(any()))
            .thenReturn(passwordEncoder.encode(request.getPassword()));
  }

  void initSourceData2() {
    when(captchaRepository.findByCode(request.getCaptcha())).thenReturn(Optional.empty());
  }

  void initSourceData3() {
    when(captchaRepository.findByCode(request.getCaptcha())).thenReturn(Optional.of(testCaptcha));
    when(userRepository.findByCode(request.getCode())).thenReturn(Optional.empty());
  }

  void initSourceData4() {
    when(captchaRepository.findByCode(request.getCaptcha())).thenReturn(Optional.of(testCaptcha));

    ReflectionTestUtils.setField(authService, "verificationCodeTime", 600L);
    ReflectionTestUtils.setField(authService, "linkPatternRepeatRestorePassword", "pattern");
    ReflectionTestUtils.setField(authService, "serverPort", "8080");

    testUser.setCodeCreateTime(LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0));
    when(userRepository.findByCode(request.getCode())).thenReturn(Optional.of(testUser));
  }

  @Test
  void testChangePasswordAfterRestoreRequestSuccess() {
    initSourceData1();
    assertThat(testUser.getCode()).isNotNull();
    assertThat(testUser.getCodeCreateTime()).isNotNull();

    ChangePassRs response = authService.changePassword(request);
    assertThat(response.isResult()).isTrue();
    assertThat(passwordEncoder.matches(request.getPassword(), testUser.getPassword())).isTrue();
    assertThat(testUser.getCode()).isNull();
    assertThat(testUser.getCodeCreateTime()).isNull();
  }

  @Test
  void testCheckingInvalidCaptcha() {
    initSourceData2();
    assertThrows(InvalidCaptchaCodeException.class, () -> authService.changePassword(request));
  }

  @Test
  void testCheckingInvalidVerificationCode() {
    initSourceData3();
    assertThrows(InvalidVerificationCodeException.class, () -> authService.changePassword(request));
  }

  @Test
  void testCheckingExpiredVerificationCode() {
    initSourceData4();
    assertThrows(VerificationCodeOutdatedException.class,
            () -> authService.changePassword(request));
  }
}