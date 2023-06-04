package ru.olshevskiy.blogengine.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * PostVoteRq.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
public class PostVoteRq {

  @JsonProperty(value = "post_id")
  @Schema(description = "Идентификатор поста, которому ставим лайк/дизлайк", example = "15")
  private int postId;
}