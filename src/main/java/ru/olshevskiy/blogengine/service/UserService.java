package ru.olshevskiy.blogengine.service;

import java.util.Map;

/**
 * Интерфейс сервиса взаимодействия с пользователями.
 */
public interface UserService {

  Map<String, Object> getCheckAuthorization();
}
