package ru.olshevskiy.blogengine.model.dto;

import java.util.Set;
import lombok.Data;

/**
 * PostByIdDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
public class PostByIdDto {

  private int id;
  private long timestamp;
  private Boolean active;
  private IdAndNameUserDto user;
  private String title;
  private String text;
  private int viewCount;
  private int likeCount;
  private int dislikeCount;
  private Set<PostCommentDto> comments;
  private Set<String> tags;
}
