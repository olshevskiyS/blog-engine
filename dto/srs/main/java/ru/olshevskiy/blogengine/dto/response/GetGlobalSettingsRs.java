package ru.olshevskiy.blogengine.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * GetGlobalSettingsRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
@Schema(description = "Получение текущих глобальных настроек блога")
public class GetGlobalSettingsRs {

  @JsonProperty("MULTIUSER_MODE")
  @Schema(description = "Разрешение регистрации новых пользователей", example = "true")
  private boolean multiuserMode;

  @JsonProperty("POST_PREMODERATION")
  @Schema(description = "Режим обязательной модерации новых постов", example = "true")
  private boolean postPremoderation;

  @JsonProperty("STATISTICS_IS_PUBLIC")
  @Schema(description = "Разрешение на получение данных статистики блога", example = "true")
  private boolean statisticsIsPublic;
}
