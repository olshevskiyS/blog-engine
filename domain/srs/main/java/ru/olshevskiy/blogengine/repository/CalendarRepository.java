package ru.olshevskiy.blogengine.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * CalendarRepository.
 *
 * @author Sergey Olshevskiy
 */
@Repository
@Transactional
@RequiredArgsConstructor
public class CalendarRepository {

  private final EntityManager manager;

  String query1 = "SELECT DISTINCT YEAR(p.time) AS years FROM Post p ";

  String query2 = "SELECT DATE_FORMAT(p.time, '%Y-%m-%d') AS dates, "
          + "COUNT(DISTINCT p.time) AS amount FROM Post p ";

  String criterion = "WHERE p.isActive = 1 "
          + "AND p.moderationStatus = 'ACCEPTED' "
          + "AND p.time <= now()";

  String additionalCondition = " AND YEAR(p.time)=%s ";

  String grouping = "GROUP BY dates";

  public List<Integer> getYearsAllActivePosts() {
    return manager.createQuery(query1 + criterion, Integer.class).getResultList();
  }

  /**
   * Method getPostsByYear.
   */
  public Map<String, Long> getPostsByYear(String year) {
    return manager.createQuery(query2 + criterion + String.format(additionalCondition, year)
            + grouping, Tuple.class).getResultStream().collect(Collectors.toMap(
                    tuple -> tuple.get("dates").toString(),
                    tuple -> (Long) tuple.get("amount")));
  }
}
