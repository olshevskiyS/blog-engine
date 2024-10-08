package ru.olshevskiy.blogengine;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithUserDetails;
import ru.olshevskiy.blogengine.dto.response.GetPostsRs;
import ru.olshevskiy.blogengine.dto.response.ModerationPostsRs;
import ru.olshevskiy.blogengine.dto.response.MyPostsRs;
import ru.olshevskiy.blogengine.dto.response.PostByIdRs;
import ru.olshevskiy.blogengine.dto.response.PostsByDateRs;
import ru.olshevskiy.blogengine.dto.response.PostsByQueryRs;
import ru.olshevskiy.blogengine.dto.response.PostsByTagRs;
import ru.olshevskiy.blogengine.repository.PostRepository;
import ru.olshevskiy.blogengine.service.PostService;

/**
 * ApiPostControllerTest.
 *
 * @author Sergey Olshevskiy
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PostServiceIntegrationTest extends BaseIntegrationTestWithTestContainer {

  @Autowired
  private PostService postService;

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
    LocalDateTime dateTheMostRecentPost = LocalDateTime.of(2022, Month.JANUARY, 17, 22, 8, 15);
    assertThat(postsRs1.getPosts().getFirst().getTimestamp())
            .isEqualTo(dateTheMostRecentPost.toEpochSecond(ZoneOffset.UTC));

    GetPostsRs postsRs2 = postService.getPosts(0, 10, "early");
    LocalDateTime dateTheMostEarlyPost = LocalDateTime.of(2021, Month.DECEMBER, 18, 9, 12, 50);
    assertThat(postsRs2.getPosts().getFirst().getTimestamp())
            .isEqualTo(dateTheMostEarlyPost.toEpochSecond(ZoneOffset.UTC));

    GetPostsRs postsRs3 = postService.getPosts(0, 10, "popular");
    assertThat(postsRs3.getPosts().getFirst().getId()).isEqualTo(5);
    assertThat(postsRs3.getPosts().getFirst().getCommentCount()).isEqualTo(2);

    GetPostsRs postsRs4 = postService.getPosts(0, 10, "best");
    assertThat(postsRs4.getPosts().getFirst().getId()).isEqualTo(1);
    assertThat(postsRs4.getPosts().getFirst().getLikeCount()).isEqualTo(2);
  }

  @Test
  void testGetPostsPagination() {
    GetPostsRs postsRs = postService.getPosts(2, 2, "recent");
    assertThat(postsRs.getPosts().size()).isEqualTo(2);
    assertThat(postsRs.getPosts().getFirst().getId()).isEqualTo(3);
    LocalDateTime dateFirstPostOnPage = LocalDateTime.of(2021, Month.DECEMBER, 18, 19, 2, 34);
    assertThat(postsRs.getPosts().getFirst().getTimestamp())
            .isEqualTo(dateFirstPostOnPage.toEpochSecond(ZoneOffset.UTC));
  }

  @Test
  void testGetPostsByQuery() {
    PostsByQueryRs postsRs1 = postService.getPostsByQuery(0, 10, "коррозии");
    assertThat(postsRs1.getCount()).isEqualTo(2L);
    assertThat(postsRs1.getPosts().size()).isEqualTo(2);
    assertThat(postsRs1.getPosts().getFirst().getId()).isEqualTo(5);

    PostsByQueryRs postsRs2 = postService.getPostsByQuery(0, 10, "query");
    assertThat(postsRs2.getCount()).isEqualTo(0L);
    assertThat(postsRs2.getPosts().size()).isEqualTo(0);

    PostsByQueryRs postsRs3 = postService.getPostsByQuery(0, 10, " ");
    assertThat(postsRs3.getCount()).isEqualTo(5L);
    assertThat(postsRs3.getPosts().size()).isEqualTo(5);
    assertThat(postsRs3.getPosts().getFirst().getId()).isEqualTo(7);
  }

  @Test
  void testGetPostsByDate() {
    PostsByDateRs postsRs = postService.getPostsByDate(0, 10, "2021-12-18");
    assertThat(postsRs.getCount()).isEqualTo(3L);
    assertThat(postsRs.getPosts().size()).isEqualTo(3);
    assertThat(postsRs.getPosts().getFirst().getId()).isEqualTo(3);
  }

  @Test
  void testGetPostsByTag() {
    PostsByTagRs postsRs = postService.getPostsByTag(0, 10, "коррозия");
    assertThat(postsRs.getCount()).isEqualTo(2L);
    assertThat(postsRs.getPosts().size()).isEqualTo(2);
    assertThat(postsRs.getPosts().getFirst().getId()).isEqualTo(5);
  }

  @Test
  void testGetPostByIdUnauthorizedUser() {
    PostByIdRs postsRs = postService.getPostById(2);
    assertThat(postsRs.getViewCount()).isEqualTo(7);
    assertThat(postsRs.getUser().getName()).isEqualTo("пользователь01");
    assertThat(postsRs.getComments().size()).isEqualTo(0);
    assertThat(postsRs.getTags().stream().findFirst().orElseThrow()).isEqualTo("правила");
  }

  @Test
  @WithUserDetails(value = "user02@email.com",
                   userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testGetPostByIdAuthorizedUserAsNotAuthor() {
    PostByIdRs postsRs = postService.getPostById(2);
    assertThat(postsRs.getViewCount()).isEqualTo(7);
  }

  @Test
  @WithUserDetails(value = "user01@email.com",
                   userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testGetPostByIdAuthorizedUserAsAuthor() {
    PostByIdRs postsRs = postService.getPostById(2);
    assertThat(postsRs.getViewCount()).isEqualTo(6);
  }

  @Test
  @WithUserDetails(value = "user02@email.com",
                   userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testGetMyPostsWithUser2() {
    MyPostsRs myPostsRs1 = postService.getMyPosts(0, 10, "inactive");
    assertThat(myPostsRs1.getCount()).isEqualTo(0L);
    MyPostsRs myPostsRs2 = postService.getMyPosts(0, 10, "pending");
    assertThat(myPostsRs2.getCount()).isEqualTo(1L);
    MyPostsRs myPostsRs3 = postService.getMyPosts(0, 10, "declined");
    assertThat(myPostsRs3.getCount()).isEqualTo(0L);
    MyPostsRs myPostsRs4 = postService.getMyPosts(0, 10, "published");
    assertThat(myPostsRs4.getCount()).isEqualTo(2L);
  }

  @Test
  @WithUserDetails(value = "user03@email.com",
          userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testGetMyPostsWithUser3() {
    MyPostsRs myPostsRs1 = postService.getMyPosts(0, 10, "inactive");
    assertThat(myPostsRs1.getCount()).isEqualTo(1L);
    MyPostsRs myPostsRs2 = postService.getMyPosts(0, 10, "pending");
    assertThat(myPostsRs2.getCount()).isEqualTo(0L);
    MyPostsRs myPostsRs3 = postService.getMyPosts(0, 10, "declined");
    assertThat(myPostsRs3.getCount()).isEqualTo(1L);
    MyPostsRs myPostsRs4 = postService.getMyPosts(0, 10, "published");
    assertThat(myPostsRs4.getCount()).isEqualTo(1L);
  }

  @Test
  @WithUserDetails(value = "user01@email.com",
          userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testGetModerationPosts() {
    ModerationPostsRs moderationPostsRs1 = postService.getModerationPosts(0, 10, "new");
    assertThat(moderationPostsRs1.getCount()).isEqualTo(1L);
    ModerationPostsRs moderationPostsRs2 = postService.getModerationPosts(0, 10, "declined");
    assertThat(moderationPostsRs2.getCount()).isEqualTo(1L);
    ModerationPostsRs moderationPostsRs3 = postService.getModerationPosts(0, 10, "accepted");
    assertThat(moderationPostsRs3.getCount()).isEqualTo(5L);
  }
}