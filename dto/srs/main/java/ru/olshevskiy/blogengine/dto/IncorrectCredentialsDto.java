package ru.olshevskiy.blogengine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * IncorrectCredentialsDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Schema(description = "Ответ сервера в случае неверных учетных данных пользователя")
public class IncorrectCredentialsDto {

  @Schema(description = "Статус ответа", example = "false")
  private boolean result;
}
