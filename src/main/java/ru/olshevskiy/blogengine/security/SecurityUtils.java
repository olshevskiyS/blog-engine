package ru.olshevskiy.blogengine.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * SecurityUtils.
 *
 * @author Sergey Olshevskiy
 */
public class SecurityUtils {

  public static boolean userIsNotAuthorized() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth == null || auth.getPrincipal() instanceof String;
  }

  public static User getCurrentUser() {
    return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
