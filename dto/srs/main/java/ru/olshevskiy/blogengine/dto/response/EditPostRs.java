package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * EditPostRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
@Schema(description = "Ответ сервера в случае успешного редактирования поста")
public class EditPostRs {

  @Schema(description = "Статус ответа", example = "true")
  private boolean result;
}
