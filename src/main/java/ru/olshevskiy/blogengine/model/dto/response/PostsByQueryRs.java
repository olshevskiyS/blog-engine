package ru.olshevskiy.blogengine.model.dto.response;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.olshevskiy.blogengine.model.dto.PostDto;

/**
 * PostsByQueryRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
public class PostsByQueryRs {

  private long count;
  private List<PostDto> posts;
}
