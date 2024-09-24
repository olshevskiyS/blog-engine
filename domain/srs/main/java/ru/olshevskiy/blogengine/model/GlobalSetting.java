package ru.olshevskiy.blogengine.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

/**
 * GlobalSetting.
 *
 * @author Sergey Olshevskiy
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "global_settings")
public class GlobalSetting {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "code", nullable = false)
  private String code;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "value", nullable = false)
  private String value;

  /**
   * GlobalSetting constructor.
   */
  public GlobalSetting(String code, String name, String value) {
    this.code = code;
    this.name = name;
    this.value = value;
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
    GlobalSetting comparedGlobalSetting = (GlobalSetting) that;
    return getId() != null && Objects.equals(getId(), comparedGlobalSetting.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
            : getClass().hashCode();
  }
}