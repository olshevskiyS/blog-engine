package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * AddPostCommentRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
@Schema(description = "Ответ сервера в случае успешного создания нового комментария")
public class AddPostCommentRs {

  @Schema(description = "Идентификатор созданного комментария", example = "36")
  private int id;
}
