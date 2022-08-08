package ru.olshevskiy.blogengine.service;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.olshevskiy.blogengine.model.dto.GlobalSettingDto;
import ru.olshevskiy.blogengine.repository.GlobalSettingRepository;

/**
 * GlobalSettingServiceImpl.
 *
 * @author Sergey Olshevskiy
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GlobalSettingServiceImpl implements GlobalSettingService {

  private final GlobalSettingRepository globalSettingRepository;

  @Override
  public GlobalSettingDto getGlobalSettings() {
    log.info("Start request getGlobalSettings");
    GlobalSettingDto globalSettingsDto = new GlobalSettingDto();
    Map<String, Boolean> globalSettings = new HashMap<>();
    globalSettingRepository.findAll().forEach(setting -> {
      boolean value;
      value = setting.getValue().equals("YES");
      globalSettings.put(setting.getCode(), value);
    });
    globalSettingsDto.setMultiuserMode(globalSettings.get("MULTIUSER_MODE"))
            .setPostPremoderation(globalSettings.get("POST_PREMODERATION"))
            .setStatisticsIsPublic(globalSettings.get("STATISTICS_IS_PUBLIC"));
    log.info("Finish request getGlobalSettings");
    return globalSettingsDto;
  }
}
