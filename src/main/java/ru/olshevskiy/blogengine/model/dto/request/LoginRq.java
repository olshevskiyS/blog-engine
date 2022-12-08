package ru.olshevskiy.blogengine.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * LoginRq.
 *
 * @author Sergey Olshevskiy
 */
@Data
public class LoginRq {

  @JsonProperty(value = "e_mail")
  @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
  private String email;

  @Size(min = 6)
  private String password;
}
