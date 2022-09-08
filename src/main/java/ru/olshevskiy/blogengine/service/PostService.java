package ru.olshevskiy.blogengine.service;

import ru.olshevskiy.blogengine.model.dto.GetPostsDto;
import ru.olshevskiy.blogengine.model.dto.PostsByQueryDto;

/**
 * PostService.
 *
 * @author Sergey Olshevskiy
 */
public interface PostService {

  GetPostsDto getPosts(int offset, int limit, String mode);

  PostsByQueryDto getPostsByQuery(int offset, int limit, String query);
}
