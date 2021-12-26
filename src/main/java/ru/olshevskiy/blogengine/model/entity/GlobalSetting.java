package ru.olshevskiy.blogengine.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс сущности глобальных настроек блога.
 */
@Getter
@Setter
@NoArgsConstructor
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

  GlobalSetting(String code, String name, String value) {
    this.code = code;
    this.name = name;
    this.value = value;
  }
}