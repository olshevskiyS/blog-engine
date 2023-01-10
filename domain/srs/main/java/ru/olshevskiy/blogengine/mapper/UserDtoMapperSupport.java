package ru.olshevskiy.blogengine.mapper;

import ru.olshevskiy.blogengine.model.User;

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
