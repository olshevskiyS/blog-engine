package ru.olshevskiy.blogengine;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneOffset;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
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
@ActiveProfiles("test")
@Sql(scripts = "/create-test-data.sql")
@Sql(scripts = "/clean-test-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class CreateAndEditPostIntegrationTest extends InitTestContainer {

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
  EditPostRq editPostRq = new EditPostRq();

  @BeforeEach
  void setup() {
    createPostRq1.setTimestamp(1894681025L)
                .setActive((byte) 1)
                .setTitle("Новый пост")
                .setText("Публикация записей на стене - один из основных вариантов, чтобы"
                         + " поделиться со своей аудиторией новой информацией")
                .setTags(new ArrayList<>() {{
                    add("цинкование");
                    add("пост");
                  }}
        );

    createPostRq2.setTimestamp(1894681035L)
                 .setActive((byte) 0)
                 .setTitle("Новый пост 2")
                 .setText("Текст поста")
                 .setTags(new ArrayList<>());

    editPostRq.setTimestamp(1894681026L)
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
  void testCreateNewActivePostByUser() {
    CreatePostRs createPostRs = postService.createPost(createPostRq1);
    assertThat(createPostRs.isResult()).isTrue();

    Post newPost = postRepository.getById(9);
    assertThat(newPost.getTime().toEpochSecond(ZoneOffset.UTC)).isEqualTo(1894681025L);
    assertThat(newPost.getIsActive()).isEqualTo((byte) 1);
    assertThat(newPost.getTitle()).isEqualTo("Новый пост");
    assertThat(newPost.getModerationStatus()).isEqualTo(ModerationStatus.NEW);
    assertThat(newPost.getViewCount()).isEqualTo(0);
    assertThat(newPost.getModerator()).isNotNull();

    Tag newTag = tagRepository.getById(5);
    assertThat(newTag.getName()).isEqualTo("пост");
    assertThat(newTag.getPosts().contains(newPost)).isTrue();
    assertThat(newPost.getTags().size()).isEqualTo(2);
    assertThat(newPost.getTags().contains(newTag)).isTrue();

    User author = userRepository.getById(2);
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

    Post newPost = postRepository.getById(9);
    assertThat(newPost.getIsActive()).isEqualTo((byte) 0);
    assertThat(newPost.getModerationStatus()).isEqualTo(ModerationStatus.NEW);
    assertThat(newPost.getModerator()).isNull();
  }

  @Test
  @WithUserDetails(value = "user01@email.com",
          userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testCreateNewActivePostByModerator() {
    CreatePostRs createPostRs = postService.createPost(createPostRq1);
    assertThat(createPostRs.isResult()).isTrue();

    Post newPost = postRepository.getById(9);
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

    Post newPost = postRepository.getById(9);
    assertThat(newPost.getIsActive()).isEqualTo((byte) 0);
    assertThat(newPost.getModerationStatus()).isEqualTo(ModerationStatus.NEW);
    assertThat(newPost.getModerator()).isNull();
  }

  @Test
  @WithUserDetails(value = "user02@email.com",
          userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testEditPostByUser() {
    Post originalPost = postRepository.getById(3);
    assertThat(originalPost.getModerationStatus()).isEqualTo(ModerationStatus.ACCEPTED);
    assertThat(originalPost.getTags().size()).isEqualTo(2);

    EditPostRs editPostRs = postService.editPost(3, editPostRq);
    assertThat(editPostRs.isResult()).isTrue();

    Post updatedPost = postRepository.getById(3);
    assertThat(updatedPost.getTime().toEpochSecond(ZoneOffset.UTC)).isEqualTo(1894681026L);
    assertThat(updatedPost.getIsActive()).isEqualTo((byte) 0);
    assertThat(updatedPost.getText()).isEqualTo("2");
    assertThat(updatedPost.getModerationStatus()).isEqualTo(ModerationStatus.NEW);
    assertThat(updatedPost.getTags().size()).isEqualTo(3);

    Tag newTag = tagRepository.getById(5);
    assertThat(newTag.getName()).isEqualTo("новый");
    assertThat(newTag.getPosts().contains(updatedPost)).isTrue();
    assertThat(updatedPost.getTags().contains(newTag)).isTrue();
  }

  @Test
  @WithUserDetails(value = "user01@email.com",
          userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testEditPostByModerator() {
    Post originalPost = postRepository.getById(3);
    assertThat(originalPost.getModerationStatus()).isEqualTo(ModerationStatus.ACCEPTED);

    EditPostRs editPostRs = postService.editPost(3, editPostRq);
    assertThat(editPostRs.isResult()).isTrue();

    Post updatedPost = postRepository.getById(3);
    assertThat(updatedPost.getTitle()).isEqualTo("1");
    assertThat(updatedPost.getModerationStatus()).isEqualTo(ModerationStatus.ACCEPTED);

  }
}