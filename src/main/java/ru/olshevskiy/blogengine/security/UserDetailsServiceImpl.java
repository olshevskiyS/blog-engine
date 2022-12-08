package ru.olshevskiy.blogengine.security;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.olshevskiy.blogengine.model.dto.ErrorDescription;
import ru.olshevskiy.blogengine.model.entity.User;
import ru.olshevskiy.blogengine.repository.UserRepository;

/**
 * UserDetailsServiceImpl.
 *
 * @author Sergey Olshevskiy
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email).orElseThrow(() ->
            new UsernameNotFoundException(String.format(ErrorDescription.USER_NOT_FOUND, email)));
    return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .authorities(getAuthorities(user))
            .build();
  }

  private Set<SimpleGrantedAuthority> getAuthorities(User user) {
    if (user.getIsModerator() == 1) {
      return Role.MODERATOR.getAuthorities();
    }
    return Role.USER.getAuthorities();
  }
}
