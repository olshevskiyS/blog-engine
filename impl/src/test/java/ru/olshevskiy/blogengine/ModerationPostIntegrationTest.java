package ru.olshevskiy.blogengine;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.olshevskiy.blogengine.dto.request.ModerationPostRq;
import ru.olshevskiy.blogengine.dto.response.ModerationPostRs;
import ru.olshevskiy.blogengine.model.ModerationStatus;
import ru.olshevskiy.blogengine.model.Post;
import ru.olshevskiy.blogengine.repository.PostRepository;
import ru.olshevskiy.blogengine.service.PostService;

/**
 * ModerationPostIntegrationTest.
 *
 * @author Sergey Olshevskiy
 */
@SpringBootTest
@Transactional
public class ModerationPostIntegrationTest extends BaseIntegrationTestWithTestContainer {

  ModerationPostRq moderationPostRq1 = new ModerationPostRq();
  ModerationPostRq moderationPostRq2 = new ModerationPostRq();
  ModerationPostRq moderationPostRq3 = new ModerationPostRq();

  @Autowired
  private PostService postService;
  @Autowired
  private PostRepository postRepository;

  @BeforeEach
  void setup() {
    moderationPostRq1.setPostId(4);
    moderationPostRq1.setDecision("accept");

    moderationPostRq2.setPostId(7);
    moderationPostRq2.setDecision("decline");

    moderationPostRq3.setPostId(100);
    moderationPostRq3.setDecision("accept");
  }

  @Test
  void testAcceptPost() {
    Post currentPostBeforeModeration = postRepository.getReferenceById(4);
    assertThat(currentPostBeforeModeration.getModerationStatus()).isEqualTo(ModerationStatus.NEW);

    ModerationPostRs moderationPostRs = postService.moderatePost(moderationPostRq1);
    assertThat(moderationPostRs.isResult()).isTrue();

    Post moderatedPost = postRepository.getReferenceById(4);
    assertThat(moderatedPost.getModerationStatus()).isEqualTo(ModerationStatus.ACCEPTED);
  }

  @Test
  void testDeclinePost() {
    Post currentPostBeforeModeration = postRepository.getReferenceById(7);
    assertThat(currentPostBeforeModeration.getModerationStatus())
            .isEqualTo(ModerationStatus.ACCEPTED);

    ModerationPostRs moderationPostRs = postService.moderatePost(moderationPostRq2);
    assertThat(moderationPostRs.isResult()).isTrue();

    Post moderatedPost = postRepository.getReferenceById(7);
    assertThat(moderatedPost.getModerationStatus()).isEqualTo(ModerationStatus.DECLINED);
  }

  @Test
  void testModerationOfNonExistentPost() {
    ModerationPostRs moderationPostRs = postService.moderatePost(moderationPostRq3);
    assertThat(moderationPostRs.isResult()).isFalse();
  }
}