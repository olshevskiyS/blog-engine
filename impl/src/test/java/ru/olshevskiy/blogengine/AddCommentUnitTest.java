package ru.olshevskiy.blogengine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.olshevskiy.blogengine.dto.request.AddPostCommentRq;
import ru.olshevskiy.blogengine.dto.response.AddPostCommentRs;
import ru.olshevskiy.blogengine.exception.ex.InvalidCommentException;
import ru.olshevskiy.blogengine.exception.ex.WrongParamInputException;
import ru.olshevskiy.blogengine.model.Post;
import ru.olshevskiy.blogengine.model.PostComment;
import ru.olshevskiy.blogengine.model.User;
import ru.olshevskiy.blogengine.repository.PostCommentRepository;
import ru.olshevskiy.blogengine.repository.PostRepository;
import ru.olshevskiy.blogengine.security.SecurityUtils;
import ru.olshevskiy.blogengine.service.PostService;

/**
 * AddCommentUnitTest.
 *
 * @author Sergey Olshevskiy
 */
@ExtendWith(MockitoExtension.class)
public class AddCommentUnitTest {

  @InjectMocks
  private PostService postService;
  @Mock
  private PostRepository postRepository;
  @Mock
  private PostCommentRepository postCommentRepository;

  private MockedStatic<SecurityUtils> securityUtils;

  private final AddPostCommentRq request1 = new AddPostCommentRq();
  private final AddPostCommentRq request2 = new AddPostCommentRq();
  private final AddPostCommentRq request3 = new AddPostCommentRq();

  @BeforeEach
  void setup() {
    request1.setPostId(1);
    request1.setText("Comment1");

    request2.setPostId(2);
    request2.setText("Comment2");
    request2.setParentId(2);

    request3.setPostId(3);
    request3.setText("3");

    securityUtils = Mockito.mockStatic(SecurityUtils.class);
    User user = new User();
    user.setId(1);
    securityUtils.when(SecurityUtils::getCurrentUser).thenReturn(user);
  }

  @AfterEach
  void close() {
    securityUtils.close();
  }

  void initSourceData1() {
    Post post = new Post();
    post.setId(1);
    when(postRepository.findById(1)).thenReturn(Optional.of(post));

    PostComment savedPostComment = new PostComment();
    savedPostComment.setId(11);
    when(postCommentRepository.save(Mockito.any(PostComment.class))).thenReturn(savedPostComment);
  }

  void initSourceData2() {
    Post post = new Post();
    post.setId(2);
    when(postRepository.findById(2)).thenReturn(Optional.of(post));

    PostComment postComment = new PostComment();
    postComment.setId(2);
    when(postCommentRepository.findById(2)).thenReturn(Optional.of(postComment));

    PostComment savedPostComment = new PostComment();
    savedPostComment.setId(22);
    when(postCommentRepository.save(Mockito.any(PostComment.class))).thenReturn(savedPostComment);
  }

  void initSourceData3() {
    when(postRepository.findById(1)).thenReturn(Optional.empty());
  }

  void initSourceData4() {
    Post post = new Post();
    post.setId(2);
    when(postRepository.findById(2)).thenReturn(Optional.of(post));

    when(postCommentRepository.findById(2)).thenReturn(Optional.empty());
  }

  @Test
  void testAddCommentForPostSuccess() {
    initSourceData1();
    AddPostCommentRs response1 = postService.addPostComment(request1);
    assertThat(response1.getId()).isEqualTo(11);
  }

  @Test
  void testAddCommentForParentCommentSuccess() {
    initSourceData2();
    AddPostCommentRs response2 = postService.addPostComment(request2);
    assertThat(response2.getId()).isEqualTo(22);
  }

  @Test
  void testAddCommentForPostException() {
    initSourceData3();
    WrongParamInputException exception = assertThrows(WrongParamInputException.class,
            () -> postService.addPostComment(request1));
    assertThat(exception.getMessage()).isEqualTo("Данный пост не существует");
  }

  @Test
  void testAddCommentForParentCommentException() {
    initSourceData4();
    WrongParamInputException exception = assertThrows(WrongParamInputException.class,
            () -> postService.addPostComment(request2));
    assertThat(exception.getMessage()).isEqualTo("Данный комментарий не существует");
  }

  @Test
  void testShortLengthTextException() {
    assertThrows(InvalidCommentException.class, () -> postService.addPostComment(request3));
  }
}
