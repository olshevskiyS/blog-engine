package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ChangePassRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
@Schema(description = "Ответ сервера на запрос смены пароля в результате запроса на восстановление")
public class ChangePassRs {

  @Schema(description = "Статус ответа", example = "true")
  private boolean result;
}