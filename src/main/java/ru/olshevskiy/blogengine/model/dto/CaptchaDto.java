package ru.olshevskiy.blogengine.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * CaptchaDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
public class CaptchaDto {

  private String secret;
  private String image;
}