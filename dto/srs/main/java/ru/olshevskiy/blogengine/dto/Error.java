package ru.olshevskiy.blogengine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Error.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
@Schema(description = "Сообщение об ошибке")
public class Error {

  @Schema(description = "Статус ответа", example = "false")
  private boolean result;

  @Schema(description = "Описание ошибки", example = "captcha: Код с картинки введён неверно")
  private Map<String, String> errors;
}
