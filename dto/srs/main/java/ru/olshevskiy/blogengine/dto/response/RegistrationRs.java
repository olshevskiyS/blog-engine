package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * RegistrationRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
@Schema(description = "Ответ сервера в случае успешной регистрации пользователя")
public class RegistrationRs {

  @Schema(description = "Статус ответа", example = "true")
  private boolean result;
}
