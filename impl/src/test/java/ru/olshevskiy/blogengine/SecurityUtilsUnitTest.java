package ru.olshevskiy.blogengine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.olshevskiy.blogengine.exception.ex.UnauthorizedUserException;
import ru.olshevskiy.blogengine.repository.UserRepository;
import ru.olshevskiy.blogengine.security.SecurityUtils;

/**
 * SecurityUtilsUnitTest.
 *
 * @author Sergey Olshevskiy
 */
@ExtendWith(MockitoExtension.class)
public class SecurityUtilsUnitTest {

  @Mock
  private UserRepository userRepository;

  void setMockOutputPrincipalIsAnonymous() {
    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn("Anonymous");
    setTestContext(authentication);
  }

  void setMockOutputAuthenticationCurrentUser() throws
          NoSuchFieldException, IllegalAccessException {
    Authentication authentication = mock(Authentication.class);
    setTestUser(authentication);
    setTestContext(authentication);
    ru.olshevskiy.blogengine.model.User testUser = new ru.olshevskiy.blogengine.model.User();
    testUser.setName("User");
    when(userRepository.findByEmail("User")).thenReturn(Optional.of(testUser));
    injectUserRepository();
  }

  void setMockOutputNotFoundCurrentUser() throws
          NoSuchFieldException, IllegalAccessException {
    Authentication authentication = mock(Authentication.class);
    setTestUser(authentication);
    setTestContext(authentication);
    when(userRepository.findByEmail("User")).thenReturn(Optional.empty());
    injectUserRepository();
  }

  private void setTestContext(Authentication authentication) {
    SecurityContext context = new SecurityContextImpl();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
  }

  private void setTestUser(Authentication authentication) {
    User principal = new User("User", "1111111", new ArrayList<>());
    when(authentication.getPrincipal()).thenReturn(principal);
  }

  private void injectUserRepository() throws NoSuchFieldException, IllegalAccessException {
    Field field = SecurityUtils.class.getDeclaredField("userRepository");
    field.setAccessible(true);
    field.set(null, userRepository);
  }

  @Test
  void testUserIsNotAuthorizedWhenAuthenticationIsNull() {
    setTestContext(null);
    assertThat(SecurityUtils.userIsNotAuthorized()).isTrue();
  }

  @Test
  void testUserIsNotAuthorizedWhenPrincipalIsAnonymous() {
    setMockOutputPrincipalIsAnonymous();
    assertThat(SecurityUtils.userIsNotAuthorized()).isTrue();
  }

  @Test
  void testGetCurrentUserWhenPrincipalIsAnonymous() {
    setMockOutputPrincipalIsAnonymous();
    assertThrows(UnauthorizedUserException.class, SecurityUtils::getCurrentUser);
  }

  @Test
  void testGetCurrentUserIsOk() throws IllegalAccessException, NoSuchFieldException {
    setMockOutputAuthenticationCurrentUser();
    assertThat(SecurityUtils.userIsNotAuthorized()).isFalse();
    String currentUserName = ((User)
            SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    assertThat(SecurityUtils.getCurrentUser().getName()).isEqualTo(currentUserName);
  }

  @Test
  void testGetCurrentUserIfNotFound() throws NoSuchFieldException, IllegalAccessException {
    setMockOutputNotFoundCurrentUser();
    assertThrows(UsernameNotFoundException.class, SecurityUtils::getCurrentUser);
  }

  @AfterEach
  public void tearDown() {
    SecurityContextHolder.clearContext();
  }
}