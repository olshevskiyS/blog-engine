package ru.olshevskiy.blogengine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * UserDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Schema(description = "Данные текущего пользователя")
public class UserDto {

  @Schema(description = "Идентификатор", example = "5")
  private int id;

  @Schema(description = "Имя", example = "Сергей")
  private String name;

  @Schema(description = "Ссылка на фото", example = "null")
  private String photo;

  @Schema(description = "Email", example = "user@xxx.ru")
  private String email;

  @Schema(description = "Проверка является ли пользователь модератором", example = "true")
  private boolean moderation;

  @Schema(description = "Количество постов необходимых для проверки модератором", example = "8")
  private int moderationCount;

  @Schema(description =
      "Проверка имеет ли пользователь возможность редактировать глобальные настройки блога",
      example = "true")
  private boolean settings;
}
