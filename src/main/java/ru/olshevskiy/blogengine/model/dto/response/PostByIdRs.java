package ru.olshevskiy.blogengine.model.dto.response;

import java.util.Set;
import lombok.Data;
import ru.olshevskiy.blogengine.model.dto.IdAndNameUserDto;
import ru.olshevskiy.blogengine.model.dto.PostCommentDto;

/**
 * PostByIdRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
public class PostByIdRs {

  private int id;
  private long timestamp;
  private boolean active;
  private IdAndNameUserDto user;
  private String title;
  private String text;
  private int viewCount;
  private int likeCount;
  private int dislikeCount;
  private Set<PostCommentDto> comments;
  private Set<String> tags;
}
