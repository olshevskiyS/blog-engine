package ru.olshevskiy.blogengine.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.olshevskiy.blogengine.exception.ex.EmailDuplicateException;
import ru.olshevskiy.blogengine.exception.ex.InvalidCaptchaCodeException;
import ru.olshevskiy.blogengine.exception.ex.MultiuserModeException;
import ru.olshevskiy.blogengine.model.dto.request.RegistrationRq;
import ru.olshevskiy.blogengine.model.entity.CaptchaCode;
import ru.olshevskiy.blogengine.model.entity.GlobalSetting;
import ru.olshevskiy.blogengine.model.entity.User;
import ru.olshevskiy.blogengine.repository.CaptchaRepository;
import ru.olshevskiy.blogengine.repository.GlobalSettingRepository;
import ru.olshevskiy.blogengine.repository.UserRepository;

/**
 * RegistrationExceptionsUnitTest.
 *
 * @author Sergey Olshevskiy
 */
@ExtendWith(MockitoExtension.class)
public class RegistrationExceptionsUnitTest {

  @InjectMocks
  private AuthServiceImpl authService;

  @Mock
  private GlobalSettingRepository globalSettingRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private CaptchaRepository captchaRepository;

  RegistrationRq registrationRq = new RegistrationRq();

  {
    registrationRq.setEmail("user04@email.com")
                  .setPassword("4444444")
                  .setCaptcha("uyeq")
                  .setCaptchaSecret("dXllcQ==");
  }

  void setMockOutputForMultiuserModeTest() {
    GlobalSetting setting = new GlobalSetting("MULTIUSER_MODE", "MM", "NO");
    when(globalSettingRepository.findByCode("MULTIUSER_MODE")).thenReturn(setting);
  }

  void setMockOutputForDuplicateEmailTest() {
    GlobalSetting setting = new GlobalSetting("MULTIUSER_MODE", "MM", "YES");
    Optional<User> userOptional = Optional.of(new User());
    when(globalSettingRepository.findByCode("MULTIUSER_MODE")).thenReturn(setting);
    when(userRepository.findByEmail("user04@email.com")).thenReturn(userOptional);
  }

  void setMockOutputForInvalidCaptchaTest() {
    GlobalSetting setting = new GlobalSetting("MULTIUSER_MODE", "MM", "YES");
    Optional<User> userOptional = Optional.empty();
    Optional<CaptchaCode> captchaOptional = Optional.empty();
    when(globalSettingRepository.findByCode("MULTIUSER_MODE")).thenReturn(setting);
    when(userRepository.findByEmail("user04@email.com")).thenReturn(userOptional);
    when(captchaRepository.findByCode("uyeq")).thenReturn(captchaOptional);
  }

  @Test
  void testMultiuserModeException() {
    setMockOutputForMultiuserModeTest();
    assertThrows(MultiuserModeException.class, () -> authService.register(registrationRq));
  }

  @Test
  void testDuplicateEmailException() {
    setMockOutputForDuplicateEmailTest();
    assertThrows(EmailDuplicateException.class, () -> authService.register(registrationRq));
  }

  @Test
  void testInvalidCaptchaException() {
    setMockOutputForInvalidCaptchaTest();
    assertThrows(InvalidCaptchaCodeException.class, () -> authService.register(registrationRq));
  }
}
