package ru.olshevskiy.blogengine.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.olshevskiy.blogengine.InitTestContainer;
import ru.olshevskiy.blogengine.model.dto.response.GetPostsRs;
import ru.olshevskiy.blogengine.model.dto.response.PostByIdRs;
import ru.olshevskiy.blogengine.model.dto.response.PostsByDateRs;
import ru.olshevskiy.blogengine.model.dto.response.PostsByQueryRs;
import ru.olshevskiy.blogengine.model.dto.response.PostsByTagRs;
import ru.olshevskiy.blogengine.repository.PostRepository;

/**
 * ApiPostControllerTest.
 *
 * @author Sergey Olshevskiy
 */
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/create-test-data.sql")
@Sql(scripts = "/clean-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PostServiceIntegrationTest extends InitTestContainer {

  @Autowired
  private PostServiceImpl postService;

  @Autowired
  private PostRepository postRepository;

  @Test
  void testGetCountActivePosts() {
    int countActivePosts = postRepository.getCountActivePosts();
    assertThat(countActivePosts).isEqualTo(5);
  }

  @Test
  void testGetPostsWithVariousSortingMode() {
    GetPostsRs postsRs1 = postService.getPosts(0, 10, "recent");
    assertThat(postsRs1.getCount()).isEqualTo(5L);
    assertThat(postsRs1.getPosts().get(0).getTimestamp()).isEqualTo(1642457295L);

    GetPostsRs postsRs2 = postService.getPosts(0, 10, "early");
    assertThat(postsRs2.getPosts().get(0).getTimestamp()).isEqualTo(1639818770L);

    GetPostsRs postsRs3 = postService.getPosts(0, 10, "popular");
    assertThat(postsRs3.getPosts().get(0).getId()).isEqualTo(5);
    assertThat(postsRs3.getPosts().get(0).getCommentCount()).isEqualTo(2);

    GetPostsRs postsRs4 = postService.getPosts(0, 10, "best");
    assertThat(postsRs4.getPosts().get(0).getId()).isEqualTo(1);
    assertThat(postsRs4.getPosts().get(0).getLikeCount()).isEqualTo(2);
  }

  @Test
  void testGetPostsPagination() {
    GetPostsRs postsRs = postService.getPosts(2, 2, "recent");
    assertThat(postsRs.getPosts().size()).isEqualTo(2);
    assertThat(postsRs.getPosts().get(0).getId()).isEqualTo(3);
    assertThat(postsRs.getPosts().get(0).getTimestamp()).isEqualTo(1639854154L);
  }

  @Test
  void testGetPostsByQuery() {
    PostsByQueryRs postsRs1 = postService.getPostsByQuery(0, 10, "коррозии");
    assertThat(postsRs1.getCount()).isEqualTo(2L);
    assertThat(postsRs1.getPosts().size()).isEqualTo(2);
    assertThat(postsRs1.getPosts().get(0).getId()).isEqualTo(5);

    PostsByQueryRs postsRs2 = postService.getPostsByQuery(0, 10, "query");
    assertThat(postsRs2.getCount()).isEqualTo(0L);
    assertThat(postsRs2.getPosts().size()).isEqualTo(0);

    PostsByQueryRs postsRs3 = postService.getPostsByQuery(0, 10, " ");
    assertThat(postsRs3.getCount()).isEqualTo(5L);
    assertThat(postsRs3.getPosts().size()).isEqualTo(5);
    assertThat(postsRs3.getPosts().get(0).getId()).isEqualTo(7);
  }

  @Test
  void testGetPostsByDate() {
    PostsByDateRs postsRs = postService.getPostsByDate(0, 10, "2021-12-18");
    assertThat(postsRs.getCount()).isEqualTo(3L);
    assertThat(postsRs.getPosts().size()).isEqualTo(3);
    assertThat(postsRs.getPosts().get(0).getId()).isEqualTo(3);
  }

  @Test
  void testGetPostsByTag() {
    PostsByTagRs postsRs = postService.getPostsByTag(0, 10, "коррозия");
    assertThat(postsRs.getCount()).isEqualTo(2L);
    assertThat(postsRs.getPosts().size()).isEqualTo(2);
    assertThat(postsRs.getPosts().get(0).getId()).isEqualTo(5);
  }

  @Test
  void testGetPostById() {
    PostByIdRs postsRs = postService.getPostById(2);
    assertThat(postsRs.getViewCount()).isEqualTo(6);
    assertThat(postsRs.getUser().getName()).isEqualTo("пользователь01");
    assertThat(postsRs.getComments().size()).isEqualTo(0);
    assertThat(postsRs.getTags().stream().findFirst().orElseThrow()).isEqualTo("правила");
  }
}