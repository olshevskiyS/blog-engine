package ru.olshevskiy.blogengine.model.projection;

import ru.olshevskiy.blogengine.model.entity.Post;

/**
 * PostView.
 *
 * @author Sergey Olshevskiy
 */
public interface PostView {

  Post getPost();

  int getLikeCount();

  int getDislikeCount();
}
