package ru.olshevskiy.blogengine.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class RegistrationRq {

  @JsonProperty(value = "e_mail")
  @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
  private String email;

  @Size(min = 6)
  private String password;

  @Size(min = 2)
  private String name;

  private String captcha;

  @JsonProperty(value = "captcha_secret")
  private String captchaSecret;
}
