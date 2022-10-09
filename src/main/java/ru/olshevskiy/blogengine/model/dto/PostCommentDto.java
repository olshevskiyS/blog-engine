package ru.olshevskiy.blogengine.model.dto;

import lombok.Data;

/**
 * PostCommentDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
public class PostCommentDto {

  private int id;
  private long timestamp;
  private String text;
  private IdNamePhotoUserDto user;
}
