package ru.olshevskiy.blogengine;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.olshevskiy.blogengine.repository.CalendarRepository;

/**
 * CalendarRepositoryIntegrationTest.
 *
 * @author Sergey Olshevskiy
 */
@SpringBootTest
public class CalendarRepositoryIntegrationTest extends BaseIntegrationTestWithTestContainer {

  @Autowired
  private CalendarRepository calendarRepository;

  @Test
  void testGetYearsAllActivePosts() {
    List<Integer> years = calendarRepository.getYearsAllActivePosts();
    assertThat(years.size()).isEqualTo(2);
    assertThat(years.getFirst()).isEqualTo(2021);
  }

  @Test
  void testGetPostsByYear() {
    Map<String, Long> posts = calendarRepository.getPostsByYear("2021");
    assertThat(posts.size()).isEqualTo(2);
    assertThat(posts.get("2021-12-18")).isEqualTo(3);
  }
}
