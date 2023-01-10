package ru.olshevskiy.blogengine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * PostCommentDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Schema(description = "Данные комментария")
public class PostCommentDto {

  @Schema(description = "Идентификатор", example = "4")
  private int id;

  @Schema(description = "Метка времени создания", example = "1592338866")
  private long timestamp;

  @Schema(description = "Текст комментария", example = "Текст")
  private String text;

  private IdNamePhotoUserDto user;
}
