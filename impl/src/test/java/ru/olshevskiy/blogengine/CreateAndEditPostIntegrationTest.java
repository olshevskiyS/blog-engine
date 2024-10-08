package ru.olshevskiy.blogengine;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.olshevskiy.blogengine.dto.request.CreatePostRq;
import ru.olshevskiy.blogengine.dto.request.EditPostRq;
import ru.olshevskiy.blogengine.dto.response.CreatePostRs;
import ru.olshevskiy.blogengine.dto.response.EditPostRs;
import ru.olshevskiy.blogengine.model.ModerationStatus;
import ru.olshevskiy.blogengine.model.Post;
import ru.olshevskiy.blogengine.model.Tag;
import ru.olshevskiy.blogengine.model.User;
import ru.olshevskiy.blogengine.repository.PostRepository;
import ru.olshevskiy.blogengine.repository.TagRepository;
import ru.olshevskiy.blogengine.repository.UserRepository;
import ru.olshevskiy.blogengine.service.PostService;

/**
 * CreateAndEditPostIntegrationTest.
 *
 * @author Sergey Olshevskiy
 */
@SpringBootTest
@Transactional
public class CreateAndEditPostIntegrationTest extends BaseIntegrationTestWithTestContainer {

  @Autowired
  private PostService postService;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private TagRepository tagRepository;
  @Autowired
  private UserRepository userRepository;

  CreatePostRq createPostRq1 = new CreatePostRq();
  CreatePostRq createPostRq2 = new CreatePostRq();
  CreatePostRq createPostRq3 = new CreatePostRq();
  EditPostRq editPostRq = new EditPostRq();
  long timeOfPostponedPost = Instant.now().plusSeconds(3540).getEpochSecond();

  @BeforeEach
  void setup() {
    createPostRq1.setTimestamp(Instant.now().minusSeconds(3630).getEpochSecond())
                 .setActive((byte) 1)
                 .setTitle("Новый пост")
                 .setText("Публикация записей на стене - один из основных вариантов, чтобы"
                     + " поделиться со своей аудиторией новой информацией")
                 .setTags(new ArrayList<>() {{
                     add("цинкование");
                     add("пост");
                   }}
        );

    createPostRq2.setTimestamp(Instant.now().minusSeconds(2490).getEpochSecond())
                 .setActive((byte) 0)
                 .setTitle("Новый пост 2")
                 .setText("Текст поста")
                 .setTags(new ArrayList<>());

    createPostRq3.setTimestamp(timeOfPostponedPost)
                 .setActive((byte) 1)
                 .setTitle("Новый пост 3")
                 .setText("Текст поста")
                 .setTags(new ArrayList<>());

    editPostRq.setTimestamp(Instant.now().minusSeconds(4170).getEpochSecond())
              .setActive((byte) 0)
              .setTitle("1")
              .setText("2")
              .setTags(new ArrayList<>() {{
                  add("цинкование");
                  add("правила");
                  add("новый");
                }}
        );
  }

  @Test
  @WithUserDetails(value = "user02@email.com",
          userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testCreateNewActivePostByUserIfPostPremoderationModeIsActive() {
    CreatePostRs createPostRs = postService.createPost(createPostRq1);
    assertThat(createPostRs.isResult()).isTrue();
    performChecksForTestCreateNewActivePostByUser(ModerationStatus.NEW);
  }

  @Test
  @WithUserDetails(value = "user02@email.com",
          userDetailsServiceBeanName = "userDetailsServiceImpl")
  @Sql(statements = "UPDATE global_settings SET value = 'NO' WHERE code = 'POST_PREMODERATION';")
  void testCreateNewActivePostByUserIfPostPremoderationModeIsNotActive() {
    CreatePostRs createPostRs = postService.createPost(createPostRq1);
    assertThat(createPostRs.isResult()).isTrue();
    performChecksForTestCreateNewActivePostByUser(ModerationStatus.ACCEPTED);
  }

  private void performChecksForTestCreateNewActivePostByUser(ModerationStatus moderationStatus) {
    Post newPost = postRepository.getReferenceById(9);
    assertThat(newPost.getTime().toEpochSecond(ZoneOffset.UTC))
            .isEqualTo(Instant.now().getEpochSecond());
    assertThat(newPost.getIsActive()).isEqualTo((byte) 1);
    assertThat(newPost.getTitle()).isEqualTo("Новый пост");
    assertThat(newPost.getModerationStatus()).isEqualTo(moderationStatus);
    assertThat(newPost.getViewCount()).isEqualTo(0);
    assertThat(newPost.getModerator()).isNotNull();

    Tag newTag = tagRepository.getReferenceById(5);
    assertThat(newTag.getName()).isEqualTo("пост");
    assertThat(newTag.getPosts().contains(newPost)).isTrue();
    assertThat(newPost.getTags().size()).isEqualTo(2);
    assertThat(newPost.getTags().contains(newTag)).isTrue();

    User author = userRepository.getReferenceById(2);
    assertThat(author.getPosts().contains(newPost)).isTrue();
    assertThat(author.getPosts().size()).isEqualTo(4);
    assertThat(newPost.getUserId()).isEqualTo(2);
  }

  @Test
  @WithUserDetails(value = "user02@email.com",
          userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testCreateNewHiddenPostByUser() {
    CreatePostRs createPostRs = postService.createPost(createPostRq2);
    assertThat(createPostRs.isResult()).isTrue();

    Post newPost = postRepository.getReferenceById(9);
    assertThat(newPost.getIsActive()).isEqualTo((byte) 0);
    assertThat(newPost.getModerationStatus()).isEqualTo(ModerationStatus.NEW);
    assertThat(newPost.getModerator()).isNull();
  }

  @Test
  @WithUserDetails(value = "user02@email.com",
          userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testCreateNewActivePostponedPostByUser() {
    CreatePostRs createPostRs = postService.createPost(createPostRq3);
    assertThat(createPostRs.isResult()).isTrue();

    Post newPost = postRepository.getReferenceById(9);
    assertThat(newPost.getTime().toEpochSecond(ZoneOffset.UTC)).isEqualTo(timeOfPostponedPost);
    assertThat(newPost.getTitle()).isEqualTo("Новый пост 3");
  }

  @Test
  @WithUserDetails(value = "user01@email.com",
          userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testCreateNewActivePostByModerator() {
    CreatePostRs createPostRs = postService.createPost(createPostRq1);
    assertThat(createPostRs.isResult()).isTrue();

    Post newPost = postRepository.getReferenceById(9);
    assertThat(newPost.getIsActive()).isEqualTo((byte) 1);
    assertThat(newPost.getModerationStatus()).isEqualTo(ModerationStatus.ACCEPTED);
    assertThat(newPost.getModerator().getId()).isEqualTo(1);
  }

  @Test
  @WithUserDetails(value = "user01@email.com",
          userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testCreateNewHiddenPostByModerator() {
    CreatePostRs createPostRs = postService.createPost(createPostRq2);
    assertThat(createPostRs.isResult()).isTrue();

    Post newPost = postRepository.getReferenceById(9);
    assertThat(newPost.getIsActive()).isEqualTo((byte) 0);
    assertThat(newPost.getModerationStatus()).isEqualTo(ModerationStatus.NEW);
    assertThat(newPost.getModerator()).isNull();
  }

  @Test
  @WithUserDetails(value = "user02@email.com",
          userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testEditPostByUser() {
    Post originalPost = postRepository.getReferenceById(3);
    assertThat(originalPost.getModerationStatus()).isEqualTo(ModerationStatus.ACCEPTED);
    assertThat(originalPost.getTags().size()).isEqualTo(2);

    EditPostRs editPostRs = postService.editPost(3, editPostRq);
    assertThat(editPostRs.isResult()).isTrue();

    Post updatedPost = postRepository.getReferenceById(3);
    assertThat(updatedPost.getTime().toEpochSecond(ZoneOffset.UTC))
            .isEqualTo(Instant.now().getEpochSecond());
    assertThat(updatedPost.getIsActive()).isEqualTo((byte) 0);
    assertThat(updatedPost.getText()).isEqualTo("2");
    assertThat(updatedPost.getModerationStatus()).isEqualTo(ModerationStatus.NEW);
    assertThat(updatedPost.getTags().size()).isEqualTo(3);

    Tag newTag = tagRepository.getReferenceById(5);
    assertThat(newTag.getName()).isEqualTo("новый");
    assertThat(newTag.getPosts().contains(updatedPost)).isTrue();
    assertThat(updatedPost.getTags().contains(newTag)).isTrue();
  }

  @Test
  @WithUserDetails(value = "user01@email.com",
          userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testEditPostByModerator() {
    Post originalPost = postRepository.getReferenceById(3);
    assertThat(originalPost.getModerationStatus()).isEqualTo(ModerationStatus.ACCEPTED);

    EditPostRs editPostRs = postService.editPost(3, editPostRq);
    assertThat(editPostRs.isResult()).isTrue();

    Post updatedPost = postRepository.getReferenceById(3);
    assertThat(updatedPost.getTitle()).isEqualTo("1");
    assertThat(updatedPost.getModerationStatus()).isEqualTo(ModerationStatus.ACCEPTED);

  }
}