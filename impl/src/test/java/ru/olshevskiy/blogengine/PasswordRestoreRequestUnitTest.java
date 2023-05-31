package ru.olshevskiy.blogengine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.olshevskiy.blogengine.dto.request.RestorePassRq;
import ru.olshevskiy.blogengine.dto.response.RestorePassRs;
import ru.olshevskiy.blogengine.exception.ex.WrongParamInputException;
import ru.olshevskiy.blogengine.model.User;
import ru.olshevskiy.blogengine.repository.UserRepository;
import ru.olshevskiy.blogengine.service.AuthService;
import ru.olshevskiy.blogengine.service.EmailService;

/**
 * PasswordRestoreRequestUnitTest.
 *
 * @author Sergey Olshevskiy
 */
@ExtendWith(MockitoExtension.class)
public class PasswordRestoreRequestUnitTest {

  @InjectMocks
  private AuthService authService;
  @Mock
  private EmailService emailService;
  @Mock
  private UserRepository userRepository;

  private final RestorePassRq request1 = new RestorePassRq();
  private final RestorePassRq request2 = new RestorePassRq();

  private final User testUser = new User();

  void initSourceData1() {
    request1.setEmail("recipient@test.com");
    when(userRepository.findByEmail(request1.getEmail())).thenReturn(Optional.of(testUser));

    ReflectionTestUtils.setField(authService, "linkPatternRestorePassword", "pattern");
    ReflectionTestUtils.setField(authService, "serverPort", "8080");
    ReflectionTestUtils.setField(authService, "messageText", "text");

    doNothing().when(emailService).sendMessage(Mockito.any(), Mockito.any(), Mockito.any());
  }

  void initSourceData2() {
    request2.setEmail("recipient@");
  }

  void initSourceData3() {
    request1.setEmail("recipient@test.com");
    when(userRepository.findByEmail(request1.getEmail())).thenReturn(Optional.empty());
  }

  @Test
  void testSendingMailForRestorePasswordSuccess() {
    initSourceData1();

    assertThat(testUser.getCode()).isNull();
    assertThat(testUser.getCodeCreateTime()).isNull();

    RestorePassRs response = authService.restorePassword(request1);
    assertThat(response.isResult()).isTrue();
    assertThat(testUser.getCode()).isNotNull();
    assertThat(testUser.getCodeCreateTime()).isNotNull();

    verify(emailService, times(1)).sendMessage(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  void testCheckingCorrectnessEmail() {
    initSourceData2();

    WrongParamInputException exception = assertThrows(WrongParamInputException.class,
            () -> authService.restorePassword(request2));
    assertThat(exception.getMessage()).isEqualTo("Некорректный формат e-mail");
  }

  @Test
  void testCheckingExistenceUser() {
    initSourceData3();

    RestorePassRs response = authService.restorePassword(request1);
    assertThat(response.isResult()).isFalse();
  }
}