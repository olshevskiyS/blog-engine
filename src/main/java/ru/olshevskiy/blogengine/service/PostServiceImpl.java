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
import ru.olshevskiy.blogengine.model.dto.GetPostsDto;
import ru.olshevskiy.blogengine.model.dto.PostByIdDto;
import ru.olshevskiy.blogengine.model.dto.PostDto;
import ru.olshevskiy.blogengine.model.dto.PostsByDateDto;
import ru.olshevskiy.blogengine.model.dto.PostsByQueryDto;
import ru.olshevskiy.blogengine.model.dto.PostsByTagDto;
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
  public GetPostsDto getPosts(int offset, int limit, String mode) {
    log.info("Start request getPosts with offset = " + offset
            + ", limit = " + limit + ", sorting mode = " + mode);
    GetPostsDto postsDto = new GetPostsDto();
    int pageNumber = offset / limit;
    Page<PostView> page = postRepository.getActivePosts(
            PageRequest.of(pageNumber, limit, sortingMode(mode)));
    List<PostDto> postsList = page.getContent().stream()
            .map(postViewPostDtoMapper::postViewToPostDto).collect(Collectors.toList());
    postsDto.setCount(page.getTotalElements())
            .setPosts(postsList);
    log.info("Finish request getPosts");
    return postsDto;
  }

  @Override
  public PostsByQueryDto getPostsByQuery(int offset, int limit, String query) {
    log.info("Start request getPostsByQuery with offset = " + offset
            + ", limit = " + limit + ", query = " + query);
    PostsByQueryDto postsDto = new PostsByQueryDto();
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
    postsDto.setCount(page.getTotalElements())
            .setPosts(postsList);
    log.info("Finish request getPostsByQuery = " + query);
    return postsDto;
  }

  @Override
  public PostsByDateDto getPostsByDate(int offset, int limit, String date) {
    log.info("Start request getPostsByDate with offset = " + offset
            + ", limit = " + limit + ", date = " + date);
    PostsByDateDto postsDto = new PostsByDateDto();
    Sort sortingMode = Sort.by(Sort.Direction.DESC, "time");
    int pageNumber = offset / limit;
    Page<PostView> page = postRepository.getActivePostsByDate(date,
            PageRequest.of(pageNumber, limit, sortingMode));
    List<PostDto> postsList = page.getContent().stream()
            .map(postViewPostDtoMapper::postViewToPostDto).collect(Collectors.toList());
    postsDto.setCount(page.getTotalElements())
            .setPosts(postsList);
    log.info("Finish request getPostsByDate = " + date);
    return postsDto;
  }

  @Override
  public PostsByTagDto getPostsByTag(int offset, int limit, String tag) {
    log.info("Start request getPostsByTag with offset = " + offset
            + ", limit = " + limit + ", tag = " + tag);
    PostsByTagDto postsDto = new PostsByTagDto();
    Sort sortingMode = Sort.by(Sort.Direction.DESC, "time");
    int pageNumber = offset / limit;
    Page<PostView> page = postRepository.getActivePostsByTag(tag,
            PageRequest.of(pageNumber, limit, sortingMode));
    List<PostDto> postsList = page.getContent().stream()
            .map(postViewPostDtoMapper::postViewToPostDto).collect(Collectors.toList());
    postsDto.setCount(page.getTotalElements())
            .setPosts(postsList);
    log.info("Finish request getPostsByTag = " + tag);
    return postsDto;
  }

  @Override
  public PostByIdDto getPostById(int id) {
    log.info("Start request getPostById, id = " + id);
    PostView postView = postRepository.getPostById(id).orElseThrow(PostNotFoundException::new);
    PostByIdDto postDto = postViewPostDtoMapper.postViewToPostByIdDto(postView);
    log.info("Finish request getPostById = " + id);
    return postDto;
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
