package ru.olshevskiy.blogengine.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.olshevskiy.blogengine.dto.request.CreatePostRq;
import ru.olshevskiy.blogengine.dto.response.CreatePostRs;
import ru.olshevskiy.blogengine.dto.response.GetPostsRs;
import ru.olshevskiy.blogengine.dto.response.ModerationPostsRs;
import ru.olshevskiy.blogengine.dto.response.MyPostsRs;
import ru.olshevskiy.blogengine.dto.response.PostByIdRs;
import ru.olshevskiy.blogengine.dto.response.PostsByDateRs;
import ru.olshevskiy.blogengine.dto.response.PostsByQueryRs;
import ru.olshevskiy.blogengine.dto.response.PostsByTagRs;
import ru.olshevskiy.blogengine.service.PostService;

/**
 * PostResource.
 *
 * @author Sergey Olshevskiy
 */
@RestController
@RequiredArgsConstructor
public class PostResource implements ApiPostController {

  private final PostService postService;

  @Override
  public ResponseEntity<GetPostsRs> getPosts(int offset, int limit, String mode) {
    return new ResponseEntity<>(postService.getPosts(offset, limit, mode), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<PostsByQueryRs> getPostsByQuery(int offset, int limit, String query) {
    return new ResponseEntity<>(postService.getPostsByQuery(offset, limit, query), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<PostsByDateRs> getPostsByDate(int offset, int limit, String date) {
    return new ResponseEntity<>(postService.getPostsByDate(offset, limit, date), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<PostsByTagRs> getPostsByTag(int offset, int limit, String tag) {
    return new ResponseEntity<>(postService.getPostsByTag(offset, limit, tag), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<PostByIdRs> getPostById(int id) {
    return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<MyPostsRs> getMyPosts(int offset, int limit, String status) {
    return new ResponseEntity<>(postService.getMyPosts(offset, limit, status), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ModerationPostsRs> getModerationPosts(
          int offset, int limit, String status) {
    return new ResponseEntity<>(postService.getModerationPosts(offset, limit, status),
                                HttpStatus.OK);
  }

  @Override
  public ResponseEntity<CreatePostRs> createPost(CreatePostRq createPostRq) {
    return new ResponseEntity<>(postService.createPost(createPostRq), HttpStatus.CREATED);
  }
}
