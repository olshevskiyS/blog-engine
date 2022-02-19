package ru.olshevskiy.blogengine.service;

import java.util.Map;

/**
 * Интерфейс сервиса взаимодействия с постами блога.
 */
public interface PostService {

  Map<String, Object> getPosts(int offset, int limit, String mode);
}
