package ru.olshevskiy.blogengine.service;

import com.github.cage.Cage;
import com.github.cage.image.Painter;
import java.time.LocalDateTime;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.olshevskiy.blogengine.model.dto.CaptchaDto;
import ru.olshevskiy.blogengine.model.entity.CaptchaCode;
import ru.olshevskiy.blogengine.repository.CaptchaRepository;

/**
 * CaptchaServiceImpl.
 *
 * @author Sergey Olshevskiy
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

  @Value("${blog.captcha.width}")
  private int width;

  @Value("${blog.captcha.height}")
  private int height;

  @Value("${blog.captcha.prefix}")
  private String prefix;

  @Value("${blog.captcha.valid-time}")
  private long validTime;

  private final CaptchaRepository captchaRepository;

  @Override
  public CaptchaDto getCaptcha() {
    log.info("Start request getCaptcha");
    Painter painter = new Painter(width, height, null, null, null, null);
    Cage cage = new Cage(painter, null, null, null, null, null, null);
    String code = cage.getTokenGenerator().next().substring(0, 4);
    byte[] imageBuff = cage.draw(code);
    String enCodeBase64 = Base64.getEncoder().encodeToString(imageBuff);
    String image = prefix + enCodeBase64;
    String secretCode = Base64.getEncoder().encodeToString(code.getBytes());
    CaptchaDto captchaDto = new CaptchaDto();
    captchaDto.setSecret(secretCode)
              .setImage(image);
    CaptchaCode captcha = new CaptchaCode();
    captcha.setTime(LocalDateTime.now())
           .setCode(code)
           .setSecretCode(secretCode);
    captchaRepository.deleteByTimeBefore(LocalDateTime.now().minusHours(validTime));
    captchaRepository.save(captcha);
    log.info("Finish request getCaptcha");
    return captchaDto;
  }
}
