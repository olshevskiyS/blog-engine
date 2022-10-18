package ru.olshevskiy.blogengine.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import ru.olshevskiy.blogengine.InitTestContainer;
import ru.olshevskiy.blogengine.model.dto.CaptchaDto;
import ru.olshevskiy.blogengine.repository.CaptchaRepository;

/**
 * CaptchaServiceIntegrationTest.
 *
 * @author Sergey Olshevskiy
 */
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/create-test-data.sql")
@Sql(scripts = "/clean-test-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CaptchaServiceIntegrationTest extends InitTestContainer {

  @Autowired
  private CaptchaServiceImpl captchaService;

  @Autowired
  private CaptchaRepository captchaRepository;

  @Test
  void testGetCaptcha() {
    assertThat(captchaRepository.findAll().size()).isEqualTo(1);

    CaptchaDto captchaDto = captchaService.getCaptcha();
    assertThat(captchaDto.getImage()).containsPattern("^data:image");
    String secretCode = captchaDto.getSecret();
    assertThat(secretCode).isNotNull();

    assertThat(captchaRepository.findAll().size()).isEqualTo(1);

    String code = captchaRepository.findAll().get(0).getCode();
    String verificationSecretCode = Base64.getEncoder().encodeToString(code.getBytes());
    assertThat(secretCode).isEqualTo(verificationSecretCode);
  }
}
