package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * CreatePostRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
@Schema(description = "Ответ сервера в случае успешного создания нового поста")
public class CreatePostRs {

  @Schema(description = "Статус ответа", example = "true")
  private boolean result;
}
