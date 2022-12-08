package ru.olshevskiy.blogengine.model.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * RegistrationRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
public class RegistrationRs {

  private boolean result;
}
