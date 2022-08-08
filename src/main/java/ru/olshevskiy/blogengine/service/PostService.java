package ru.olshevskiy.blogengine.service;

import ru.olshevskiy.blogengine.model.dto.GetPostsDto;

/**
 * PostService.
 *
 * @author Sergey Olshevskiy
 */
public interface PostService {

  GetPostsDto getPosts(int offset, int limit, String mode);
}
