package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.olshevskiy.blogengine.dto.UserDto;

/**
 * LoginAndCheckRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
@Schema(description =
    "Ответ сервера на запрос пользователя для входа в систему или проверки статуса аутентификации")
public class LoginAndCheckRs {

  @Schema(description = "Статус ответа", example = "true")
  private boolean result;

  private UserDto user;
}
