package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * EditProfileRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
@Schema(description = "Ответ сервера в случае успешного редактирования профиля пользователя")
public class EditProfileRs {

  @Schema(description = "Статус ответа", example = "true")
  private boolean result;
}
