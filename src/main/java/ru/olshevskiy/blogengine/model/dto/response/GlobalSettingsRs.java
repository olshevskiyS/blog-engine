package ru.olshevskiy.blogengine.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * GlobalSettingsRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
public class GlobalSettingsRs {

  @JsonProperty("MULTIUSER_MODE")
  private boolean multiuserMode;

  @JsonProperty("POST_PREMODERATION")
  private boolean postPremoderation;

  @JsonProperty("STATISTICS_IS_PUBLIC")
  private boolean statisticsIsPublic;
}
