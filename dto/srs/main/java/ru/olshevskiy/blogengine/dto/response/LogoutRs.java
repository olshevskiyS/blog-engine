package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * LogoutRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Schema(description = "Ответ сервера на запрос пользователя для выхода из системы")
public class LogoutRs {

  @Schema(description = "Статус ответа", example = "true")
  private boolean result;
}
