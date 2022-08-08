package ru.olshevskiy.blogengine.model.dto;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * GetPostsDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
public class GetPostsDto {

  Integer count;
  List<PostDto> posts;
}
