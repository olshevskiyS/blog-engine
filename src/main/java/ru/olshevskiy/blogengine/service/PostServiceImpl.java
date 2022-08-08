package ru.olshevskiy.blogengine.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import ru.olshevskiy.blogengine.model.dto.GetPostsDto;
import ru.olshevskiy.blogengine.model.dto.PostDto;
import ru.olshevskiy.blogengine.model.mapper.PostViewPostDtoMapper;
import ru.olshevskiy.blogengine.repository.PostRepository;

/**
 * PostServiceImpl.
 *
 * @author Sergey Olshevskiy
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final PostViewPostDtoMapper postViewPostDtoMapper;

  @Override
  public GetPostsDto getPosts(int offset, int limit, String mode) {
    log.info("Start request getAllActivePostsBySorting with offset = " + offset
            + ", limit = " + limit + ", sorting mode = " + mode);
    GetPostsDto postsDto = new GetPostsDto();
    int countPosts = postRepository.getCountAllActivePosts();
    int countPages = countPosts / limit;
    List<PostDto> postsList = postRepository
        .getAllActivePostsWithCommentsAndVotes(
             PageRequest.of(countPages, limit, sortingMode(mode)))
        .stream().map(postViewPostDtoMapper::postViewToPostDto)
        .collect(Collectors.toList());
    postsDto.setCount(countPosts).setPosts(postsList);
    log.info("Finish request getAllActivePostsBySorting");
    return postsDto;
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
