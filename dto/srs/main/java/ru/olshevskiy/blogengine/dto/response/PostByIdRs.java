package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.Data;
import ru.olshevskiy.blogengine.dto.IdAndNameUserDto;
import ru.olshevskiy.blogengine.dto.PostCommentDto;

/**
 * PostByIdRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Schema(description = "Полные данные поста")
public class PostByIdRs {

  @Schema(description = "Идентификатор", example = "15")
  private int id;

  @Schema(description = "Метка времени публикации", example = "1592338706")
  private long timestamp;

  @Schema(description = "Проверка опубликован ли пост", example = "true")
  private boolean active;

  private IdAndNameUserDto user;

  @Schema(description = "Заголовок", example = "Текст")
  private String title;

  @Schema(description = "Текст поста", example = "Текст")
  private String text;

  @Schema(description = "Количество просмотров", example = "10")
  private int viewCount;

  @Schema(description = "Количество лайков", example = "6")
  private int likeCount;

  @Schema(description = "Количество дизлайков", example = "1")
  private int dislikeCount;

  @Schema(description = "Список комметариев")
  private Set<PostCommentDto> comments;

  @Schema(description = "Список тэгов", example = "tags: [технология, электролиз]")
  private Set<String> tags;
}
