package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * RestorePassRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
@Schema(description = "Ответ сервера на запрос восстановления пароля")
public class RestorePassRs {

  @Schema(description = "Статус ответа", example = "true")
  private boolean result;
}