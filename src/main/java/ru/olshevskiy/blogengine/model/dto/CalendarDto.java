package ru.olshevskiy.blogengine.model.dto;

import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * CalendarDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
public class CalendarDto {

  private List<Integer> years;
  private Map<String, Long> posts;
}
