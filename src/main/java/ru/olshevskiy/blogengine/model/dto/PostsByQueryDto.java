package ru.olshevskiy.blogengine.model.dto;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * PostsByQueryDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
public class PostsByQueryDto {

  long count;
  List<PostDto> posts;
}
