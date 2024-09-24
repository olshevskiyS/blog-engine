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
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.proxy.HibernateProxy;

/**
 * CaptchaCode.
 *
 * @author Sergey Olshevskiy
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@Entity
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

  @Override
  public final boolean equals(Object that) {
    if (this == that) {
      return true;
    }
    if (that == null) {
      return false;
    }
    Class<?> thatEffectiveClass = that instanceof HibernateProxy
        ? ((HibernateProxy) that).getHibernateLazyInitializer().getPersistentClass()
        : that.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
        : this.getClass();
    if (thisEffectiveClass != thatEffectiveClass) {
      return false;
    }
    CaptchaCode comparedCaptchaCode = (CaptchaCode) that;
    return getId() != null && Objects.equals(getId(), comparedCaptchaCode.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
      ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
      : getClass().hashCode();
  }
}