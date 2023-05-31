package ru.olshevskiy.blogengine;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.testcontainers.containers.MySQLContainer;

/**
 * BaseIntegrationTestWithTestContainer.
 *
 * @author Sergey Olshevskiy
 */
@ActiveProfiles("test")
@Sql(scripts = "/create-test-data.sql")
@Sql(scripts = "/clean-test-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BaseIntegrationTestWithTestContainer {

  private static final MySQLContainer<?> mySQLContainer;

  static {
    mySQLContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("testDataBase")
            .withUsername("user")
            .withPassword("testPassword")
            .withReuse(true);

    mySQLContainer.start();
  }

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("DB_URL", mySQLContainer::getJdbcUrl);
    registry.add("DB_USERNAME", mySQLContainer::getUsername);
    registry.add("DB_PASSWORD", mySQLContainer::getPassword);
  }
}