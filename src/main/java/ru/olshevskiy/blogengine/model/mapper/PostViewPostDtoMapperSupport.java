package ru.olshevskiy.blogengine.model.mapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import ru.olshevskiy.blogengine.model.projection.PostView;

/**
 * Вспомогательный класс для интерфейса маппинга сущности Post в PostDto
 * через проекцию PostProjection.
 */
public class PostViewPostDtoMapperSupport {

  private static final int announceLength = 150;

  static long convertTimeToSeconds(PostView postView) {
    LocalDateTime time = postView.getPost().getTime();
    return time.atOffset(ZoneOffset.UTC).toInstant().getEpochSecond();
  }

  static String getAnnounceFromText(PostView postView) {
    String text = postView.getPost().getText();
    String textWithDeletedHtmlTags = text.replaceAll("<[^>]*>", "");
    return textWithDeletedHtmlTags.length() > announceLength
           ? textWithDeletedHtmlTags.substring(0, announceLength) + "..."
           : textWithDeletedHtmlTags;
  }
}
