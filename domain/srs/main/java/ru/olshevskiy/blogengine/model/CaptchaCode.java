package ru.olshevskiy.blogengine.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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

  CaptchaCode(String code, String secretCode) {
    this.code = code;
    this.secretCode = secretCode;
    time = LocalDateTime.now();
  }
}