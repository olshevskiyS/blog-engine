package ru.olshevskiy.blogengine.security;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Role.
 *
 * @author Sergey Olshevskiy
 */
public enum Role {
  USER(Set.of(Permission.WRITE)),
  MODERATOR(Set.of(Permission.WRITE, Permission.MODERATE));

  private final Set<Permission> permissions;

  Role(Set<Permission> permissions) {
    this.permissions = permissions;
  }

  public Set<Permission> getPermissions() {
    return permissions;
  }

  /**
   * Set of authorities getter.
   */
  public Set<SimpleGrantedAuthority> getAuthorities() {
    return permissions
            .stream()
            .map(p -> new SimpleGrantedAuthority(p.getPermission()))
            .collect(Collectors.toSet());
  }
}
