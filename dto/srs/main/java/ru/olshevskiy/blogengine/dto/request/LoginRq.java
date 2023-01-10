package ru.olshevskiy.blogengine.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * LoginRq.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Schema(description = "Данные пользователя для входа в систему")
public class LoginRq {

  @JsonProperty(value = "e_mail")
  @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
  @Schema(description = "Email пользователя", example = "user@xxx.ru")
  private String email;

  @Size(min = 6)
  @Schema(description = "Пароль пользователя", example = "1234567")
  private String password;
}
