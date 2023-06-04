package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * PostVoteRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
public class PostVoteRs {

  @Schema(description = "Статус ответа", example = "true")
  private boolean result;
}