package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.olshevskiy.blogengine.dto.TagDto;

/**
 * TagsByQueryRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
@Schema(description = "Список тэгов по заданной строке")
public class TagsByQueryRs {

  @Schema(description = "Полученный список тэгов")
  private List<TagDto> tags;
}
