package ru.olshevskiy.blogengine.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * CaptchaCode.
 *
 * @author Sergey Olshevskiy
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Accessors(chain = true)
@Table(name = "captcha_codes")
public class CaptchaCode {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "time", nullable = false)
  private LocalDateTime time;

  @Column(name = "code", nullable = false, columnDefinition = "tinytext")
  private String code;

  @Column(name = "secret_code", nullable = false, columnDefinition = "tinytext")
  private String secretCode;

  /**
   * New captchaCode constructor.
   */
  public CaptchaCode(String code, String secretCode) {
    this.code = code;
    this.secretCode = secretCode;
    time = LocalDateTime.now(ZoneId.ofOffset("UTC", ZoneOffset.UTC));
  }
}