package ru.olshevskiy.blogengine.model.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * CaptchaRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
public class CaptchaRs {

  private String secret;
  private String image;
}