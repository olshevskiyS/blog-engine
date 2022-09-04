package ru.olshevskiy.blogengine.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.olshevskiy.blogengine.InitTestContainer;
import ru.olshevskiy.blogengine.model.dto.GetPostsDto;

/**
 * ApiPostControllerTest.
 *
 * @author Sergey Olshevskiy
 */
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/create-test-data.sql")
@Sql(scripts = "/clean-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostServiceIntegrationTest extends InitTestContainer {

  @Autowired
  private PostServiceImpl postService;

  @Test
  void testGetPostsRecentMode() {
    assertThat(postService).isNotNull();
    GetPostsDto postsDto = postService.getPosts(0, 10, "recent");
    assertThat(postsDto.getCount().intValue()).isEqualTo(4);
    assertThat(postsDto.getPosts().get(0).getTimestamp()).isEqualTo(1639955887L);
  }

  @Test
  void testGetPostsEarlyMode() {
    assertThat(postService).isNotNull();
    GetPostsDto postsDto = postService.getPosts(0, 10, "early");
    assertThat(postsDto.getCount().intValue()).isEqualTo(4);
    assertThat(postsDto.getPosts().get(0).getTimestamp()).isEqualTo(1639818770L);
  }

  @Test
  void testGetPostsPopularMode() {
    assertThat(postService).isNotNull();
    GetPostsDto postsDto = postService.getPosts(0, 10, "popular");
    assertThat(postsDto.getCount().intValue()).isEqualTo(4);
    assertThat(postsDto.getPosts().get(0).getId()).isEqualTo(5);
    assertThat(postsDto.getPosts().get(0).getCommentCount()).isEqualTo(2);
  }

  @Test
  void testGetPostsBestMode() {
    assertThat(postService).isNotNull();
    GetPostsDto postsDto = postService.getPosts(0, 10, "best");
    assertThat(postsDto.getCount().intValue()).isEqualTo(4);
    assertThat(postsDto.getPosts().get(0).getId()).isEqualTo(1);
    assertThat(postsDto.getPosts().get(0).getLikeCount()).isEqualTo(2);
  }

  @Test
  void testPagination() {
    assertThat(postService).isNotNull();
    GetPostsDto postsDto = postService.getPosts(2, 2, "recent");
    assertThat(postsDto.getPosts().size()).isEqualTo(2);
    assertThat(postsDto.getPosts().get(0).getId()).isEqualTo(2);
    assertThat(postsDto.getPosts().get(0).getTimestamp()).isEqualTo(1639819680L);
  }
}