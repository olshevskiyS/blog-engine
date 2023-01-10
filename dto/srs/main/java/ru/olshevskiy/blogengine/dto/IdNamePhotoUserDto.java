package ru.olshevskiy.blogengine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * IdNamePhotoUserDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Schema(description = "Идентификатор, имя и ссылка на фото пользователя")
public class IdNamePhotoUserDto {

  @Schema(description = "Идентификатор", example = "5")
  private int id;

  @Schema(description = "Имя", example = "Сергей")
  private String name;

  @Schema(description = "Ссылка на фото", example = "null")
  private String photo;
}
