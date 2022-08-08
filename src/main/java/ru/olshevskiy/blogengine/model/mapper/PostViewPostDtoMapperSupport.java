package ru.olshevskiy.blogengine.model.mapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Value;
import ru.olshevskiy.blogengine.model.projection.PostView;

/**
 * PostViewPostDtoMapperSupport.
 *
 * @author Sergey Olshevskiy
 */
public class PostViewPostDtoMapperSupport {

  @Value("${blog.announceLength}")
  private int announceLength;

  private static int ANNOUNCE_LENGTH_STATIC;

  @Value("${blog.announceLength}")
  public void setAnnounceLengthStatic(int announceLength) {
    PostViewPostDtoMapperSupport.ANNOUNCE_LENGTH_STATIC = announceLength;
  }

  static long convertTimeToSeconds(PostView postView) {
    LocalDateTime time = postView.getPost().getTime();
    return time.atOffset(ZoneOffset.UTC).toInstant().getEpochSecond();
  }

  static String getAnnounceFromText(PostView postView) {
    String text = postView.getPost().getText();
    String textWithDeletedHtmlTags = text.replaceAll("<[^>]*>", "");
    return textWithDeletedHtmlTags.length() > ANNOUNCE_LENGTH_STATIC
           ? textWithDeletedHtmlTags.substring(0, ANNOUNCE_LENGTH_STATIC) + "..."
           : textWithDeletedHtmlTags;
  }
}
