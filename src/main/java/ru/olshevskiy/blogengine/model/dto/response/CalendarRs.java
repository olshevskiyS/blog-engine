package ru.olshevskiy.blogengine.model.dto.response;

import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * CalendarRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
public class CalendarRs {

  private List<Integer> years;
  private Map<String, Long> posts;
}
