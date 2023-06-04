package ru.olshevskiy.blogengine;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;
import ru.olshevskiy.blogengine.dto.request.DislikePostRq;
import ru.olshevskiy.blogengine.dto.request.LikePostRq;
import ru.olshevskiy.blogengine.dto.response.DislikePostRs;
import ru.olshevskiy.blogengine.dto.response.LikePostRs;
import ru.olshevskiy.blogengine.model.PostVote;
import ru.olshevskiy.blogengine.repository.PostVoteRepository;
import ru.olshevskiy.blogengine.service.PostService;

/**
 * AddVoteIntegrationTest.
 *
 * @author Sergey Olshevskiy
 */
@SpringBootTest
@Transactional
@WithUserDetails(value = "user02@email.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
public class AddVoteIntegrationTest extends BaseIntegrationTestWithTestContainer {

  @Autowired
  private PostService postService;
  @Autowired
  private PostVoteRepository postVoteRepository;

  private final LikePostRq likeRequest1 = new LikePostRq();
  private final LikePostRq likeRequest2 = new LikePostRq();
  private final LikePostRq likeRequest3 = new LikePostRq();
  private final LikePostRq likeRequest4 = new LikePostRq();

  private final DislikePostRq dislikeRequest1 = new DislikePostRq();
  private final DislikePostRq dislikeRequest2 = new DislikePostRq();
  private final DislikePostRq dislikeRequest3 = new DislikePostRq();

  @BeforeEach
  void setup() {
    likeRequest1.setPostId(2);
    likeRequest2.setPostId(100);
    likeRequest3.setPostId(1);
    likeRequest4.setPostId(5);

    dislikeRequest1.setPostId(2);
    dislikeRequest2.setPostId(5);
    dislikeRequest3.setPostId(1);
  }

  @Test
  void testAddNewLikeSuccess() {
    LikePostRs response = postService.like(likeRequest1);
    assertThat(response.isResult()).isTrue();

    PostVote createdLike = postVoteRepository.getById(5);
    assertThat(createdLike).isNotNull();
    assertThat(createdLike.getPostId()).isEqualTo(2);
    assertThat(createdLike.getUserId()).isEqualTo(2);
    assertThat(createdLike.getValue()).isEqualTo((byte) 1);
  }

  @Test
  void testAddNewDisLikeSuccess() {
    DislikePostRs response = postService.dislike(dislikeRequest1);
    assertThat(response.isResult()).isTrue();

    PostVote createdDislike = postVoteRepository.getById(5);
    assertThat(createdDislike).isNotNull();
    assertThat(createdDislike.getValue()).isEqualTo((byte) -1);
  }

  @Test
  void testAddLikeForDoNotExistingPost() {
    LikePostRs response = postService.like(likeRequest2);
    assertThat(response.isResult()).isFalse();
  }

  @Test
  void testAddRepeatedLike() {
    LikePostRs response = postService.like(likeRequest3);
    assertThat(response.isResult()).isFalse();
  }

  @Test
  void testAddRepeatedDisLike() {
    DislikePostRs response = postService.dislike(dislikeRequest2);
    assertThat(response.isResult()).isFalse();
  }

  @Test
  void testReplaceLikeToDislike() {
    PostVote originalVote = postVoteRepository.getById(4);
    assertThat(originalVote.getValue()).isEqualTo((byte) -1);

    LikePostRs response = postService.like(likeRequest4);
    assertThat(response.isResult()).isTrue();

    PostVote editedVote = postVoteRepository.getById(4);
    assertThat(editedVote.getValue()).isEqualTo((byte) 1);
  }

  @Test
  void testReplaceDisLikeToLike() {
    PostVote originalVote = postVoteRepository.getById(1);
    assertThat(originalVote.getValue()).isEqualTo((byte) 1);

    DislikePostRs response = postService.dislike(dislikeRequest3);
    assertThat(response.isResult()).isTrue();

    PostVote editedVote = postVoteRepository.getById(1);
    assertThat(editedVote.getValue()).isEqualTo((byte) -1);
  }
}