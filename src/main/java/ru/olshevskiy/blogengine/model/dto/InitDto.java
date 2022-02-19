package ru.olshevskiy.blogengine.model.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Компонент контекста Spring. Содержит общую информацию о блоге.
 */
@Data
@Component
public class InitDto {

  @Value("${blog.title}")
  private String title;

  @Value("${blog.subtitle}")
  private String subtitle;

  @Value("${blog.phone}")
  private String phone;

  @Value("${blog.email}")
  private String email;

  @Value("${blog.copyright}")
  private String copyright;

  @Value("${blog.copyrightFrom}")
  private String copyrightFrom;
}
