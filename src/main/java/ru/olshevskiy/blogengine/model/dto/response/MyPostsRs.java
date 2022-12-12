package ru.olshevskiy.blogengine.model.dto.response;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.olshevskiy.blogengine.model.dto.PostDto;

/**
 * MyPostsRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
public class MyPostsRs {

  private long count;
  private List<PostDto> posts;
}
