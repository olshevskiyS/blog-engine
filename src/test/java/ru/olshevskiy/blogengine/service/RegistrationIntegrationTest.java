package ru.olshevskiy.blogengine.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import ru.olshevskiy.blogengine.InitTestContainer;
import ru.olshevskiy.blogengine.model.dto.request.RegistrationRq;
import ru.olshevskiy.blogengine.model.dto.response.RegistrationRs;
import ru.olshevskiy.blogengine.model.entity.User;
import ru.olshevskiy.blogengine.repository.UserRepository;

/**
 * RegistrationIntegrationTest.
 *
 * @author Sergey Olshevskiy
 */
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/create-test-data.sql")
@Sql(scripts = "/clean-test-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RegistrationIntegrationTest extends InitTestContainer {

  @Autowired
  private AuthServiceImpl authService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  RegistrationRq registrationRq;

  @BeforeEach
  void setup() {
    registrationRq = new RegistrationRq();
    registrationRq.setEmail("user04@email.com")
                  .setPassword("4444444")
                  .setName("пользователь04")
                  .setCaptcha("uyeq")
                  .setCaptchaSecret("dXllcQ==");
  }

  @Test
  void testSuccessfulRegistration() {
    RegistrationRs registrationRs = authService.register(registrationRq);
    assertThat(registrationRs.isResult()).isTrue();
    Optional<User> optionalUser = userRepository.findByEmail(registrationRq.getEmail());
    assertThat(optionalUser.isPresent()).isTrue();
    assertThat(passwordEncoder.matches(registrationRq.getPassword(),
               optionalUser.get().getPassword())).isTrue();
  }
}
