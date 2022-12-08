package ru.olshevskiy.blogengine.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.olshevskiy.blogengine.exception.ex.PostNotFoundException;
import ru.olshevskiy.blogengine.model.dto.PostDto;
import ru.olshevskiy.blogengine.model.dto.response.GetPostsRs;
import ru.olshevskiy.blogengine.model.dto.response.PostByIdRs;
import ru.olshevskiy.blogengine.model.dto.response.PostsByDateRs;
import ru.olshevskiy.blogengine.model.dto.response.PostsByQueryRs;
import ru.olshevskiy.blogengine.model.dto.response.PostsByTagRs;
import ru.olshevskiy.blogengine.model.mapper.PostViewPostDtoMapper;
import ru.olshevskiy.blogengine.model.projection.PostView;
import ru.olshevskiy.blogengine.repository.PostRepository;

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
    Sort sortingMode = Sort.by(Sort.Direction.DESC, "time");
    int pageNumber = offset / limit;
    Page<PostView> page = postRepository.getActivePostsByDate(date,
            PageRequest.of(pageNumber, limit, sortingMode));
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
    Sort sortingMode = Sort.by(Sort.Direction.DESC, "time");
    int pageNumber = offset / limit;
    Page<PostView> page = postRepository.getActivePostsByTag(tag,
            PageRequest.of(pageNumber, limit, sortingMode));
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
    PostByIdRs postByIdRs = postViewPostDtoMapper.postViewToPostByIdRs(postView);
    log.info("Finish request getPostById = " + id);
    return postByIdRs;
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
}
