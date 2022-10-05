package ru.olshevskiy.blogengine.model.dto;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * PostsByTagDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
public class PostsByTagDto {

  private long count;
  private List<PostDto> posts;
}
