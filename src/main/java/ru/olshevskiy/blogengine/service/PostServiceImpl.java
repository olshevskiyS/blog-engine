package ru.olshevskiy.blogengine.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.olshevskiy.blogengine.exception.ex.PostNotFoundException;
import ru.olshevskiy.blogengine.model.dto.ErrorDescription;
import ru.olshevskiy.blogengine.model.dto.PostDto;
import ru.olshevskiy.blogengine.model.dto.response.GetPostsRs;
import ru.olshevskiy.blogengine.model.dto.response.MyPostsRs;
import ru.olshevskiy.blogengine.model.dto.response.PostByIdRs;
import ru.olshevskiy.blogengine.model.dto.response.PostsByDateRs;
import ru.olshevskiy.blogengine.model.dto.response.PostsByQueryRs;
import ru.olshevskiy.blogengine.model.dto.response.PostsByTagRs;
import ru.olshevskiy.blogengine.model.entity.ModerationStatus;
import ru.olshevskiy.blogengine.model.entity.Post;
import ru.olshevskiy.blogengine.model.entity.User;
import ru.olshevskiy.blogengine.model.mapper.PostViewPostDtoMapper;
import ru.olshevskiy.blogengine.model.projection.PostView;
import ru.olshevskiy.blogengine.repository.PostRepository;
import ru.olshevskiy.blogengine.repository.UserRepository;
import ru.olshevskiy.blogengine.security.SecurityUtils;

/**
 * PostServiceImpl.
 *
 * @author Sergey Olshevskiy
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final PostViewPostDtoMapper postViewPostDtoMapper;
  private final UserRepository userRepository;

  @Override
  public GetPostsRs getPosts(int offset, int limit, String mode) {
    log.info("Start request getPosts with offset = " + offset
            + ", limit = " + limit + ", sorting mode = " + mode);
    GetPostsRs postsRs = new GetPostsRs();
    int pageNumber = offset / limit;
    Page<PostView> page = postRepository.getActivePosts(
            PageRequest.of(pageNumber, limit, sortingMode(mode)));
    List<PostDto> postsList = page.getContent().stream()
            .map(postViewPostDtoMapper::postViewToPostDto).collect(Collectors.toList());
    postsRs.setCount(page.getTotalElements())
           .setPosts(postsList);
    log.info("Finish request getPosts");
    return postsRs;
  }

  @Override
  public PostsByQueryRs getPostsByQuery(int offset, int limit, String query) {
    log.info("Start request getPostsByQuery with offset = " + offset
            + ", limit = " + limit + ", query = " + query);
    PostsByQueryRs postsByQueryRs = new PostsByQueryRs();
    Sort sortingMode = Sort.by(Sort.Direction.DESC, "time");
    int pageNumber = offset / limit;
    Page<PostView> page;
    if (query.isEmpty() || query.matches("\\s+")) {
      page = postRepository.getActivePosts(PageRequest.of(pageNumber, limit, sortingMode));
    } else {
      page = postRepository.getActivePostsByQuery(query,
              PageRequest.of(pageNumber, limit, sortingMode));
    }
    List<PostDto> postsList = page.getContent().stream()
            .map(postViewPostDtoMapper::postViewToPostDto).collect(Collectors.toList());
    postsByQueryRs.setCount(page.getTotalElements())
                  .setPosts(postsList);
    log.info("Finish request getPostsByQuery = " + query);
    return postsByQueryRs;
  }

  @Override
  public PostsByDateRs getPostsByDate(int offset, int limit, String date) {
    log.info("Start request getPostsByDate with offset = " + offset
            + ", limit = " + limit + ", date = " + date);
    PostsByDateRs postsByDateRs = new PostsByDateRs();
    int pageNumber = offset / limit;
    Page<PostView> page = postRepository.getActivePostsByDate(date,
            PageRequest.of(pageNumber, limit, Sort.by(Sort.Direction.DESC, "time")));
    List<PostDto> postsList = page.getContent().stream()
            .map(postViewPostDtoMapper::postViewToPostDto).collect(Collectors.toList());
    postsByDateRs.setCount(page.getTotalElements())
                 .setPosts(postsList);
    log.info("Finish request getPostsByDate = " + date);
    return postsByDateRs;
  }

  @Override
  public PostsByTagRs getPostsByTag(int offset, int limit, String tag) {
    log.info("Start request getPostsByTag with offset = " + offset
            + ", limit = " + limit + ", tag = " + tag);
    PostsByTagRs postsByTagRs = new PostsByTagRs();
    int pageNumber = offset / limit;
    Page<PostView> page = postRepository.getActivePostsByTag(tag,
            PageRequest.of(pageNumber, limit, Sort.by(Sort.Direction.DESC, "time")));
    List<PostDto> postsList = page.getContent().stream()
            .map(postViewPostDtoMapper::postViewToPostDto).collect(Collectors.toList());
    postsByTagRs.setCount(page.getTotalElements())
                .setPosts(postsList);
    log.info("Finish request getPostsByTag = " + tag);
    return postsByTagRs;
  }

  @Override
  public PostByIdRs getPostById(int id) {
    log.info("Start request getPostById, id = " + id);
    PostView postView = postRepository.getPostById(id).orElseThrow(PostNotFoundException::new);
    Post updatedPost = updateCurrentPostViewCount(postView.getPost());
    PostByIdRs postByIdRs = postViewPostDtoMapper.postViewToPostById(
            postView, updatedPost.getViewCount());
    log.info("Finish request getPostById = " + id);
    return postByIdRs;
  }

  @Override
  public MyPostsRs getMyPosts(int offset, int limit, String status) {
    log.info("Start request getMyPosts with offset = " + offset
            + ", limit = " + limit + ", status = " + status);
    MyPostsRs myPostsRs = new MyPostsRs();
    int pageNumber = offset / limit;
    PageRequest pageRequest = PageRequest.of(
            pageNumber, limit, Sort.by(Sort.Direction.DESC, "time"));
    Page<PostView> page = getMyPostsDependingOnModerationStatus(status,
            SecurityUtils.getCurrentUserId(), pageRequest);
    List<PostDto> postsList = page.getContent().stream()
            .map(postViewPostDtoMapper::postViewToPostDto).collect(Collectors.toList());
    myPostsRs.setCount(page.getTotalElements())
             .setPosts(postsList);
    log.info("Finish request getMyPosts");
    return myPostsRs;
  }

  private Sort sortingMode(String mode) {
    Sort sort;
    switch (mode) {
      case "early": sort = Sort.by(Sort.Direction.ASC, "time");
      break;
      case "popular": sort = JpaSort.unsafe(Sort.Direction.DESC, "size(p.comments)")
              .and(Sort.by(Sort.Direction.DESC, "time"));
      break;
      case "best": sort = JpaSort.unsafe(Sort.Direction.DESC, "(likeCount)")
              .and(Sort.by(Sort.Direction.DESC, "time"));
      break;
      default: sort = Sort.by(Sort.Direction.DESC, "time");
    }
    return sort;
  }

  private User getCurrentUser() {
    org.springframework.security.core.userdetails.User principal =
            (org.springframework.security.core.userdetails.User) SecurityContextHolder
            .getContext().getAuthentication().getPrincipal();
    return userRepository.findByEmail(principal.getUsername()).orElseThrow(
            () -> new UsernameNotFoundException(String.format(
            ErrorDescription.USER_NOT_FOUND, principal.getUsername())));
  }

  private Post updateCurrentPostViewCount(Post currentPost) {
    if (SecurityUtils.userIsNotAuthorized()) {
      currentPost.setViewCount(currentPost.getViewCount() + 1);
      return postRepository.save(currentPost);
    }
    User currentUser = getCurrentUser();
    if (currentPost.getUserId() != currentUser.getId() && currentUser.getIsModerator() == 0) {
      currentPost.setViewCount(currentPost.getViewCount() + 1);
      return postRepository.save(currentPost);
    } else {
      return currentPost;
    }
  }

  private Page<PostView> getMyPostsDependingOnModerationStatus(String status, int userId,
                                                               PageRequest pageRequest) {
    Page<PostView> page;
    switch (status) {
      case "inactive": page = postRepository.getMyInactivePosts(userId, pageRequest);
      break;
      case "pending": page = postRepository.getMyActivePostsDependingOnStatus(
              userId, ModerationStatus.NEW, pageRequest);
      break;
      case "declined": page = postRepository.getMyActivePostsDependingOnStatus(
              userId, ModerationStatus.DECLINED, pageRequest);
      break;
      default: page = postRepository.getMyActivePostsDependingOnStatus(
              userId, ModerationStatus.ACCEPTED, pageRequest);
    }
    return page;
  }
}
