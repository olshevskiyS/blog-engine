package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.olshevskiy.blogengine.dto.PostDto;

/**
 * PostsByDateRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
@Schema(description = "Dto постов за указанную дату")
public class PostsByDateRs {

  @Schema(description = "Общее количество постов", example = "50")
  private long count;

  @Schema(description = "Список постов согласно параметрам пагинации")
  private List<PostDto> posts;
}
