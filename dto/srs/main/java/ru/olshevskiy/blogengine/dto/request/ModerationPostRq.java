package ru.olshevskiy.blogengine.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * ModerationPostsRq.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Schema(description = "Данные поста для модерации")
public class ModerationPostRq {

  @JsonProperty(value = "post_id")
  @Schema(description = "Идентификатор поста", example = "1")
  private int postId;

  @Schema(description = "Решение по посту", example = "accept")
  private String decision;
}
