package ru.olshevskiy.blogengine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.test.util.ReflectionTestUtils;
import ru.olshevskiy.blogengine.dto.request.LoginRq;
import ru.olshevskiy.blogengine.dto.response.LoginAndCheckRs;
import ru.olshevskiy.blogengine.dto.response.LogoutRs;
import ru.olshevskiy.blogengine.exception.ex.IncorrectCredentialsException;
import ru.olshevskiy.blogengine.service.AuthService;

/**
 * AuthServiceIntegrationTest.
 *
 * @author Sergey Olshevskiy
 */
@SpringBootTest
public class AuthServiceIntegrationTest extends BaseIntegrationTestWithTestContainer {

  @Autowired
  private AuthService authService;
  @Autowired
  private ApplicationContext context;

  @Value("${blog.security.remember-me-key}")
  private String key;

  private LoginRq loginRq;
  private LoginRq loginRq2;

  @BeforeEach
  void setup() {
    loginRq = new LoginRq();
    loginRq.setEmail("user01@email.com");
    loginRq.setPassword("1111111");

    loginRq2 = new LoginRq();
    loginRq2.setEmail("user10@email.com");
    loginRq2.setPassword("9999999");
  }

  @Test
  void testLoginSuccess() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    given(request.getContextPath()).willReturn("");
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    LoginAndCheckRs loginRs = authService.login(loginRq, request, response);
    assertThat(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()).isTrue();
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    assertThat(user.getUsername()).isEqualTo("user01@email.com");

    TokenBasedRememberMeServices rememberMeServices = context.getBean(
            TokenBasedRememberMeServices.class);
    assertThat(ReflectionTestUtils.getField(
            rememberMeServices, "alwaysRemember")).isEqualTo(true);
    assertThat(ReflectionTestUtils.getField(
            rememberMeServices, "tokenValiditySeconds")).isNotEqualTo(1209600);
    assertThat(rememberMeServices.getKey()).isEqualTo(key);

    assertThat(loginRs.isResult()).isTrue();
    assertThat(loginRs.getUser().isModeration()).isTrue();
    assertThat(loginRs.getUser().getModerationCount()).isEqualTo(1);
  }

  @Test
  void testLoginFail() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    given(request.getContextPath()).willReturn("");
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    assertThrows(IncorrectCredentialsException.class,
            () -> authService.login(loginRq2, request, response));
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  @Test
  @WithUserDetails(value = "user02@email.com",
                   userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testLogout() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    given(request.getContextPath()).willReturn("");
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    assertThat(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()).isTrue();
    LogoutRs logoutRs = authService.logout(request, response);
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    assertThat(logoutRs.isResult()).isTrue();
  }

  @Test
  @WithUserDetails(value = "user02@email.com",
                   userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testCheckAuthenticationSuccess() {
    LoginAndCheckRs checkRs = authService.check();
    assertThat(checkRs.isResult()).isTrue();
    assertThat(checkRs.getUser().getName()).isEqualTo("пользователь02");
    assertThat(checkRs.getUser().isModeration()).isFalse();
    assertThat(checkRs.getUser().getModerationCount()).isEqualTo(0);
  }

  @Test
  void testCheckAuthenticationFail() {
    assertThrows(IncorrectCredentialsException.class, () -> authService.check());
  }
}
