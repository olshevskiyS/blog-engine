package ru.olshevskiy.blogengine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TagDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
@AllArgsConstructor
@Schema(description = "Данные тэга")
public class TagDto {

  @Schema(description = "Имя тэга", example = "технология")
  private String name;

  @Schema(description = "Относительный нормированный вес тэга", example = "1")
  private String weight;
}