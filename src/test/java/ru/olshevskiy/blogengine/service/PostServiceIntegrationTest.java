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
import ru.olshevskiy.blogengine.model.dto.GetPostsDto;
import ru.olshevskiy.blogengine.model.dto.PostsByQueryDto;

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

  @Test
  void testGetPostsRecentMode() {
    assertThat(postService).isNotNull();

    GetPostsDto postsDto1 = postService.getPosts(0, 10, "recent");
    assertThat(postsDto1.getCount()).isEqualTo(4L);
    assertThat(postsDto1.getPosts().get(0).getTimestamp()).isEqualTo(1639955887L);

    GetPostsDto postsDto2 = postService.getPosts(0, 10, "early");
    assertThat(postsDto2.getPosts().get(0).getTimestamp()).isEqualTo(1639818770L);

    GetPostsDto postsDto3 = postService.getPosts(0, 10, "popular");
    assertThat(postsDto3.getPosts().get(0).getId()).isEqualTo(5);
    assertThat(postsDto3.getPosts().get(0).getCommentCount()).isEqualTo(2);

    GetPostsDto postsDto4 = postService.getPosts(0, 10, "best");
    assertThat(postsDto4.getPosts().get(0).getId()).isEqualTo(1);
    assertThat(postsDto4.getPosts().get(0).getLikeCount()).isEqualTo(2);
  }

  @Test
  void testGetPostsPagination() {
    assertThat(postService).isNotNull();
    GetPostsDto postsDto = postService.getPosts(2, 2, "recent");
    assertThat(postsDto.getPosts().size()).isEqualTo(2);
    assertThat(postsDto.getPosts().get(0).getId()).isEqualTo(2);
    assertThat(postsDto.getPosts().get(0).getTimestamp()).isEqualTo(1639819680L);
  }

  @Test
  void testGetPostsByQuery() {
    assertThat(postService).isNotNull();

    PostsByQueryDto postsDto1 = postService.getPostsByQuery(0, 10, "коррозии");
    assertThat(postsDto1.getCount()).isEqualTo(2L);
    assertThat(postsDto1.getPosts().size()).isEqualTo(2);
    assertThat(postsDto1.getPosts().get(0).getId()).isEqualTo(5);

    PostsByQueryDto postsDto2 = postService.getPostsByQuery(0, 10, "query");
    assertThat(postsDto2.getCount()).isEqualTo(0L);
    assertThat(postsDto2.getPosts().size()).isEqualTo(0);

    PostsByQueryDto postsDto3 = postService.getPostsByQuery(0, 10, " ");
    assertThat(postsDto3.getCount()).isEqualTo(4L);
    assertThat(postsDto3.getPosts().size()).isEqualTo(4);
    assertThat(postsDto3.getPosts().get(0).getId()).isEqualTo(5);
  }
}