package ru.olshevskiy.blogengine.model.dto;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * PostsByDateDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
public class PostsByDateDto {

  private long count;
  private List<PostDto> posts;
}
