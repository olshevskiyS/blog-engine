package ru.olshevskiy.blogengine.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * AddPostCommentRq.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Schema(description = "Данные нового комментария к посту или к комментарию")
public class AddPostCommentRq {

  @JsonProperty(value = "post_id")
  @Schema(description = "Идентификатор поста, к которому пишется комментарий", example = "1")
  private Integer postId;

  @JsonProperty(value = "parent_id")
  @Schema(description = "Идентификатор комментария, на который пишется комментарий", example = "2")
  private Integer parentId;

  @Schema(description = "Текст комментария", example = "Текст")
  private String text;
}
