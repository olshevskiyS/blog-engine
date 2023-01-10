package ru.olshevskiy.blogengine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * PostDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Schema(description = "Данные поста для предпросмотра")
public class PostDto {

  @Schema(description = "Идентификатор", example = "15")
  private int id;

  @Schema(description = "Метка времени публикации", example = "1592338706")
  private long timestamp;

  private IdAndNameUserDto user;

  @Schema(description = "Заголовок", example = "Текст")
  private String title;

  @Schema(description = "Предпросмотр", example = "Текст")
  private String announce;

  @Schema(description = "Количество просмотров", example = "10")
  private int viewCount;

  @Schema(description = "Количество лайков", example = "6")
  private int likeCount;

  @Schema(description = "Количество дизлайков", example = "1")
  private int dislikeCount;

  @Schema(description = "Количество комментариев", example = "8")
  private int commentCount;
}
