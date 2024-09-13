package ru.olshevskiy.blogengine.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * SetGlobalSettingsRq.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
@Schema(description = "Установка новых настроек блога")
public class SetGlobalSettingsRq {

  @JsonProperty("MULTIUSER_MODE")
  @Schema(description = "Разрешение регистрации новых пользователей", example = "true")
  private boolean multiuserMode;

  @JsonProperty("POST_PREMODERATION")
  @Schema(description = "Режим обязательной модерации новых постов", example = "true")
  private boolean postPremoderation;

  @JsonProperty("STATISTICS_IS_PUBLIC")
  @Schema(description = "Разрешение на получение данных статистики блога", example = "true")
  private boolean statisticsIsPublic;

  /**
   * SetGlobalSettingsRq. Convert settings to map method
   */
  public Map<String, Boolean> toMap() {
    Map<String, Boolean> map = new HashMap<>();
    map.put("MULTIUSER_MODE", multiuserMode);
    map.put("POST_PREMODERATION", postPremoderation);
    map.put("STATISTICS_IS_PUBLIC", statisticsIsPublic);
    return map;
  }
}