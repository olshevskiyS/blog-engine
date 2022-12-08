package ru.olshevskiy.blogengine.model.dto;

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
public class Error {

  private boolean result;
  private Map<String, String> errors;
}
