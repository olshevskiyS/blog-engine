package ru.olshevskiy.blogengine.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.olshevskiy.blogengine.dto.ErrorDescription;
import ru.olshevskiy.blogengine.exception.ex.UnauthorizedUserException;
import ru.olshevskiy.blogengine.repository.UserRepository;

/**
 * SecurityUtils.
 *
 * @author Sergey Olshevskiy
 */
@Component
public class SecurityUtils {

  private static UserRepository userRepository;

  @Autowired
  public void setUserRepository(UserRepository userRepository) {
    SecurityUtils.userRepository = userRepository;
  }

  public static boolean userIsNotAuthorized() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth == null || auth.getPrincipal() instanceof String;
  }

  /**
   * SecurityUtils. Getting a current user method.
   */
  public static ru.olshevskiy.blogengine.model.User getCurrentUser() {
    if (userIsNotAuthorized()) {
      throw new UnauthorizedUserException();
    }
    String userName = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                      .getUsername();
    return userRepository.findByEmail(userName).orElseThrow(() -> new UsernameNotFoundException(
           String.format(ErrorDescription.USER_NOT_FOUND, userName)));
  }
}
