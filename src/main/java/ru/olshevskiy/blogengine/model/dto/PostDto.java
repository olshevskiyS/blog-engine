package ru.olshevskiy.blogengine.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Класс DTO постов блога.
 */
@Data
public class PostDto {

  private int id;

  private long timestamp;

  @JsonProperty("user")
  private IdAndNameUserDto usersIdAndName;

  private String title;

  private String announce;

  private int viewCount;

  private int likeCount;

  private int dislikeCount;

  private int commentCount;
}
