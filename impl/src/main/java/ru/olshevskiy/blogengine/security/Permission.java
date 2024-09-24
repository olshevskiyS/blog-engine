package ru.olshevskiy.blogengine.security;

import lombok.Getter;

/**
 * Permission.
 *
 * @author Sergey Olshevskiy
 */
@Getter
public enum Permission {
  WRITE("user:write"),
  MODERATE("user:moderate");

  private final String permission;

  Permission(String permission) {
    this.permission = permission;
  }
}