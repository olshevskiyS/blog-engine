package ru.olshevskiy.blogengine.service;

import ru.olshevskiy.blogengine.model.dto.response.GetPostsRs;
import ru.olshevskiy.blogengine.model.dto.response.MyPostsRs;
import ru.olshevskiy.blogengine.model.dto.response.PostByIdRs;
import ru.olshevskiy.blogengine.model.dto.response.PostsByDateRs;
import ru.olshevskiy.blogengine.model.dto.response.PostsByQueryRs;
import ru.olshevskiy.blogengine.model.dto.response.PostsByTagRs;

/**
 * PostService.
 *
 * @author Sergey Olshevskiy
 */
public interface PostService {

  GetPostsRs getPosts(int offset, int limit, String mode);

  PostsByQueryRs getPostsByQuery(int offset, int limit, String query);

  PostsByDateRs getPostsByDate(int offset, int limit, String date);

  PostsByTagRs getPostsByTag(int offset, int limit, String tag);

  PostByIdRs getPostById(int id);

  MyPostsRs getMyPosts(int offset, int limit, String status);
}
