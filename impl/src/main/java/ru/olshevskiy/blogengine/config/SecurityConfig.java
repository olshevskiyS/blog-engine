package ru.olshevskiy.blogengine.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import ru.olshevskiy.blogengine.security.UserDetailsServiceImpl;

/**
 * SecurityConfig.
 *
 * @author Sergey Olshevskiy
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserDetailsServiceImpl userDetailsService;

  @Value("${blog.security.remember-me-key}")
  private String key;

  @Value("${blog.security.remember-me-validity-seconds}")
  private int validitySeconds;

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
               .authorizeHttpRequests(requests -> requests
                       .requestMatchers("/**").permitAll()
                       .anyRequest().authenticated())
               .formLogin(AbstractAuthenticationFilterConfigurer::disable)
               .sessionManagement(httpSecuritySessionManagementConfigurer ->
                       httpSecuritySessionManagementConfigurer
                               .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .rememberMe(remember -> remember.rememberMeServices(rememberMeServices()))
               .build();
  }

  @Bean
  protected AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    authenticationProvider.setUserDetailsService(userDetailsService);
    return authenticationProvider;
  }

  /**
   * RememberMeServices bean.
   */
  @Bean
  public TokenBasedRememberMeServices rememberMeServices() {
    TokenBasedRememberMeServices rememberMeServices =
            new TokenBasedRememberMeServices(key, userDetailsService);
    rememberMeServices.setAlwaysRemember(true);
    rememberMeServices.setTokenValiditySeconds(validitySeconds);
    return rememberMeServices;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  protected AuthenticationManager authenticationManager(
          AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}