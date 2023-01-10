package ru.olshevskiy.blogengine.security;

/**
 * Permission.
 *
 * @author Sergey Olshevskiy
 */
public enum Permission {
  WRITE("user:write"),
  MODERATE("user:moderate");

  private final String permission;

  Permission(String permission) {
    this.permission = permission;
  }

  public String getPermission() {
    return permission;
  }
}
