package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * StatisticsRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
public class StatisticsRs {

  @Schema(description = "Количество постов", example = "10")
  private int postsCount;

  @Schema(description = "Количество лайков", example = "6")
  private int likesCount;

  @Schema(description = "Количество дизлайков", example = "1")
  private int dislikesCount;

  @Schema(description = "Количество просмотров", example = "10")
  private int viewsCount;

  @Schema(description = "Метка времени первой публикации", example = "1592338706")
  private long firstPublication;
}