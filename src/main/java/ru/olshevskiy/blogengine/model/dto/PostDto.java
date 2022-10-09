package ru.olshevskiy.blogengine.model.dto;

import lombok.Data;

/**
 * PostDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
public class PostDto {

  private int id;
  private long timestamp;
  private IdAndNameUserDto user;
  private String title;
  private String announce;
  private int viewCount;
  private int likeCount;
  private int dislikeCount;
  private int commentCount;
}
