package ru.olshevskiy.blogengine.mapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.olshevskiy.blogengine.model.Post;
import ru.olshevskiy.blogengine.projection.PostView;

/**
 * PostViewMapperSupport.
 *
 * @author Sergey Olshevskiy
 */
@Component
public class PostViewMapperSupport {

  private static int announceLength;

  @Value("${blog.post.announceLength}")
  public void setAnnounceLength(int length) {
    announceLength = length;
  }

  static long convertTimeToSeconds(LocalDateTime time) {
    return time.atOffset(ZoneOffset.UTC).toInstant().getEpochSecond();
  }

  static String getAnnounceFromText(PostView postView) {
    String text = postView.getPost().getText();
    String textWithDeletedHtmlTags = text.replaceAll("<[^>]*>", "");
    return textWithDeletedHtmlTags.length() > announceLength
           ? textWithDeletedHtmlTags.substring(0, announceLength) + "..."
           : textWithDeletedHtmlTags;
  }

  static Boolean checkPostIsActive(Post post) {
    return post.getIsActive() == 1;
  }
}
