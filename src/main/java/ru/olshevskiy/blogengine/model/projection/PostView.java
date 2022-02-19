package ru.olshevskiy.blogengine.model.projection;

import ru.olshevskiy.blogengine.model.entity.Post;

/**
 * Интерфейс проекции постов блога.
 */
public interface PostView {

  Post getPost();

  int getLikeCount();

  int getDislikeCount();
}
