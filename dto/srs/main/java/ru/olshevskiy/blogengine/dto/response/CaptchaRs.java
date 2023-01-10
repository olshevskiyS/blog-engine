package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * CaptchaRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
@Schema(description = "Данные сгенерированной каптчи")
public class CaptchaRs {

  @Schema(description = "Секретный код каптчи", example = "dXllcQ==")
  private String secret;

  @Schema(description = "Изображение с кодом каптчи", example = "data:image/png;base64...")
  private String image;
}