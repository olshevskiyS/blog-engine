package ru.olshevskiy.blogengine.projection;

import ru.olshevskiy.blogengine.model.Post;

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
