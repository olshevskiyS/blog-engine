package ru.olshevskiy.blogengine;

import org.testcontainers.containers.MySQLContainer;

/**
 * InitTestContainer.
 *
 * @author Sergey Olshevskiy
 */
public class InitTestContainer {

  private static final MySQLContainer mySQLContainer = new MySQLContainer<>("mysql:latest")
          .withDatabaseName("testdb")
          .withUsername("user")
          .withPassword("tpass")
          .withReuse(true);

  static {
    mySQLContainer.start();
    System.setProperty("DB_URL", mySQLContainer.getJdbcUrl());
    System.setProperty("DB_USERNAME", mySQLContainer.getUsername());
    System.setProperty("DB_PASSWORD", mySQLContainer.getPassword());
  }
}