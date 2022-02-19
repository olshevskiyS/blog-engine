package ru.olshevskiy.blogengine.model.dto;

import lombok.Data;

/**
 * Класс DTO пользователя, передающий идентификатор и имя.
 */
@Data
public class IdAndNameUserDto {

  private Integer id;
  private String name;
}
