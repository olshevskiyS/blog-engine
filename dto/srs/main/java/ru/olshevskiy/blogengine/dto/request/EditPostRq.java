package ru.olshevskiy.blogengine.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * EditPostRq.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
@Schema(description = "Данные для редактирования поста")
public class EditPostRq {

  @Schema(description = "Новые дата и время публикации", example = "1592338706")
  private long timestamp;

  @Schema(description = "Открыть пост или скрыть", example = "1")
  private byte active;

  @Size(min = 3)
  @Schema(description = "Новый заголовок поста", example = "Текст")
  private String title;

  @Size(min = 50)
  @Schema(description = "Новый текст поста в формате HTML", example = "Текст")
  private String text;

  @Schema(description = "Список тэгов", example = "[технология, электролиз]")
  private List<String> tags;
}
