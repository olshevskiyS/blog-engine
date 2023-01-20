package ru.olshevskiy.blogengine.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * CreatePostRq.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
@Schema(description = "Данные нового поста")
public class CreatePostRq {

  @Schema(description = "Дата и время публикации", example = "1592338706")
  private long timestamp;

  @Schema(description = "Открыт пост или скрыт", example = "1")
  private byte active;

  @Size(min = 3)
  @Schema(description = "Заголовок поста", example = "Текст")
  private String title;

  @Size(min = 50)
  @Schema(description = "Текст поста в формате HTML", example = "Текст")
  private String text;

  @Schema(description = "Список тэгов", example = "[технология, электролиз]")
  private List<String> tags;
}
