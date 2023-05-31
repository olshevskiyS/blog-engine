package ru.olshevskiy.blogengine.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * RestorePassRq.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Schema(description = "Email пользователя, пароль аккаунта которого необходимо восстановить")
public class RestorePassRq {

  @Schema(description = "Email пользователя", example = "user@xxx.ru")
  private String email;
}
