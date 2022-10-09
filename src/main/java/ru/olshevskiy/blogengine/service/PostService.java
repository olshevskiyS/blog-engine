package ru.olshevskiy.blogengine.service;

import ru.olshevskiy.blogengine.model.dto.GetPostsDto;
import ru.olshevskiy.blogengine.model.dto.PostByIdDto;
import ru.olshevskiy.blogengine.model.dto.PostsByDateDto;
import ru.olshevskiy.blogengine.model.dto.PostsByQueryDto;
import ru.olshevskiy.blogengine.model.dto.PostsByTagDto;

/**
 * PostService.
 *
 * @author Sergey Olshevskiy
 */
public interface PostService {

  GetPostsDto getPosts(int offset, int limit, String mode);

  PostsByQueryDto getPostsByQuery(int offset, int limit, String query);

  PostsByDateDto getPostsByDate(int offset, int limit, String date);

  PostsByTagDto getPostsByTag(int offset, int limit, String tag);

  PostByIdDto getPostById(int id);
}
