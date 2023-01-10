package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * InitRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Component
@Schema(description = "Общая информация о блоге")
public class InitRs {

  @Value("${blog.title}")
  @Schema(description = "Название блога", example = "Текст")
  private String title;

  @Value("${blog.subtitle}")
  @Schema(description = "Подзаголовок", example = "Текст")
  private String subtitle;

  @Value("${blog.phone}")
  @Schema(description = "Номер телефона", example = "+79999999999")
  private String phone;

  @Value("${blog.email}")
  @Schema(description = "Почтовый адрес", example = "user@xxx.ru")
  private String email;

  @Value("${blog.copyright}")
  @Schema(description = "Авторское право", example = "Сергей")
  private String copyright;

  @Value("${blog.copyrightFrom}")
  @Schema(description = "Год первого опубликования", example = "2020")
  private String copyrightFrom;
}
