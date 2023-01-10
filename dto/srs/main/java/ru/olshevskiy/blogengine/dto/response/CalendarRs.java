package ru.olshevskiy.blogengine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Количество публикаций на каждую дату заданного года")
public class CalendarRs {

  @Schema(description = "Список годов, за которые были публикации", example = "[2021, 2022]")
  private List<Integer> years;

  @Schema(description = "Количество публикаций по датам", example = "{2021-12-17: 56}")
  private Map<String, Long> posts;
}
