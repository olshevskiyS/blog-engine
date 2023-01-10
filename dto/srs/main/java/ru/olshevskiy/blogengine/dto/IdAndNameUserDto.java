package ru.olshevskiy.blogengine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * IdAndNameUserDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Schema(description = "Идентификатор и имя пользователя")
public class IdAndNameUserDto {

  @Schema(description = "Идентификатор", example = "5")
  private int id;

  @Schema(description = "Имя", example = "Сергей")
  private String name;
}
