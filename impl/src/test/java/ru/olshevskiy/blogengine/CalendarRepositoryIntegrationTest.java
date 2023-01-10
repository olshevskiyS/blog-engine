package ru.olshevskiy.blogengine;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import ru.olshevskiy.blogengine.repository.CalendarRepository;

/**
 * CalendarRepositoryIntegrationTest.
 *
 * @author Sergey Olshevskiy
 */
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/create-test-data.sql")
@Sql(scripts = "/clean-test-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CalendarRepositoryIntegrationTest extends InitTestContainer {

  @Autowired
  private CalendarRepository calendarRepository;

  @Test
  void testGetYearsAllActivePosts() {
    List<Integer> years = calendarRepository.getYearsAllActivePosts();
    assertThat(years.size()).isEqualTo(2);
    assertThat(years.get(0)).isEqualTo(2021);
  }

  @Test
  void testGetPostsByYear() {
    Map<String, Long> posts = calendarRepository.getPostsByYear("2021");
    assertThat(posts.size()).isEqualTo(2);
    assertThat(posts.get("2021-12-18")).isEqualTo(3);
  }
}
