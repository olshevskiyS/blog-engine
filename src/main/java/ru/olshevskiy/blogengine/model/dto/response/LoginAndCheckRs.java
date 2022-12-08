package ru.olshevskiy.blogengine.model.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.olshevskiy.blogengine.model.dto.UserDto;

/**
 * LoginAndCheckRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
public class LoginAndCheckRs {

  private boolean result;
  private UserDto user;
}
