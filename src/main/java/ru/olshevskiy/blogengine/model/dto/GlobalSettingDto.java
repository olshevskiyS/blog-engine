package ru.olshevskiy.blogengine.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * GlobalSettingDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
public class GlobalSettingDto {

  @JsonProperty("MULTIUSER_MODE")
  private boolean multiuserMode;

  @JsonProperty("POST_PREMODERATION")
  private boolean postPremoderation;

  @JsonProperty("STATISTICS_IS_PUBLIC")
  private boolean statisticsIsPublic;
}
