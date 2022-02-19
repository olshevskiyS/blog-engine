package ru.olshevskiy.blogengine.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import ru.olshevskiy.blogengine.model.dto.PostDto;
import ru.olshevskiy.blogengine.model.mapper.PostViewPostDtoMapper;
import ru.olshevskiy.blogengine.repository.PostRepository;

/**
 * Сервис взаимодействия с постами блога.
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final PostViewPostDtoMapper postViewPostDtoMapper;

  @Override
  public Map<String, Object> getPosts(int offset, int limit, String mode) {
    Map<String, Object> postsMap = new HashMap<>();
    int countPosts = postRepository.getCountAllActivePosts();
    int countPages = countPosts / limit;
    List<PostDto> postsList = postRepository
        .getAllActivePostsWithCommentsAndVotes(
             PageRequest.of(countPages, limit, sortingMode(mode)))
        .stream().map(postViewPostDtoMapper::postViewToPostDto)
        .collect(Collectors.toList());
    postsMap.put("count", countPosts);
    postsMap.put("posts", postsList);
    return postsMap;
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
