package ru.olshevskiy.blogengine.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * RegistrationRq.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
@Schema(description = "Регистрационные данные пользователя")
public class RegistrationRq {

  @JsonProperty(value = "e_mail")
  @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
  @Schema(description = "Email пользователя", example = "user@xxx.ru")
  private String email;

  @Size(min = 6)
  @Schema(description = "Пароль пользователя", example = "1234567")
  private String password;

  @Size(min = 2)
  @Schema(description = "Имя пользователя", example = "Сергей")
  private String name;

  @Schema(description = "Текст с картинки каптчи", example = "uyeq")
  private String captcha;

  @JsonProperty(value = "captcha_secret")
  @Schema(description = "Секретный код каптчи", example = "dXllcQ==")
  private String captchaSecret;
}
