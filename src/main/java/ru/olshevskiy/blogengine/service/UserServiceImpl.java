package ru.olshevskiy.blogengine.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Сервис взаимодействия с пользователями.
 */
@Service
public class UserServiceImpl implements UserService {

  @Override
  public Map<String, Object> getCheckAuthorization() {
    Map<String, Object> map = new HashMap<>();
    map.put("result", false);
    return map;
  }
}
