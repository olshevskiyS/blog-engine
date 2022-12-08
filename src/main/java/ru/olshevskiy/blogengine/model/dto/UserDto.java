package ru.olshevskiy.blogengine.model.dto;

import lombok.Data;

/**
 * UserDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
public class UserDto {

  private int id;
  private String name;
  private String photo;
  private String email;
  private boolean moderation;
  private int moderationCount;
  private boolean settings;
}
