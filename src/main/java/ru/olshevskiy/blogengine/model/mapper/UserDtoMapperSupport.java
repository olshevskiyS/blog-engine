package ru.olshevskiy.blogengine.model.mapper;

import ru.olshevskiy.blogengine.model.entity.User;

/**
 * UserDtoMapperSupport.
 *
 * @author Sergey Olshevskiy
 */
public class UserDtoMapperSupport {

  static boolean checkModerationOption(User user) {
    return user.getIsModerator() == 1;
  }
}
