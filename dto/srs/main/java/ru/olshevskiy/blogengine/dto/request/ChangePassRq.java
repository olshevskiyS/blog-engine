package ru.olshevskiy.blogengine.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ChangePassRq.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
@Schema(description = "Данные для смены пароля в результате запроса на восстановление")
public class ChangePassRq {

  @Schema(description = "Код восстановления пароля", example = "f0a2e47a-d51a-42dc-a414")
  private String code;

  @Size(min = 6)
  @Schema(description = "Новый пароль", example = "1234567")
  private String password;

  @Schema(description = "Текст с картинки каптчи", example = "uyeq")
  private String captcha;

  @JsonProperty(value = "captcha_secret")
  @Schema(description = "Секретный код каптчи", example = "dXllcQ==")
  private String captchaSecret;
}