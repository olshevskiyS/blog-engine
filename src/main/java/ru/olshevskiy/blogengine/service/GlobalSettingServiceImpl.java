package ru.olshevskiy.blogengine.service;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.olshevskiy.blogengine.model.dto.GlobalSettingDto;
import ru.olshevskiy.blogengine.repository.GlobalSettingRepository;

/**
 * Сервис для глобальных настроек блога.
 */
@Service
@RequiredArgsConstructor
public class GlobalSettingServiceImpl implements GlobalSettingService {

  private final GlobalSettingRepository globalSettingRepository;

  @Override
  public GlobalSettingDto getGlobalSettings() {
    GlobalSettingDto globalSettingsDto = new GlobalSettingDto();
    Map<String, Boolean> globalSettings = new HashMap<>();
    globalSettingRepository.findAll().forEach(setting -> {
      boolean value;
      value = setting.getValue().equals("YES");
      globalSettings.put(setting.getCode(), value);
    });
    globalSettingsDto.setMultiuserMode(globalSettings.get("MULTIUSER_MODE"));
    globalSettingsDto.setPostPremoderation(globalSettings.get("POST_PREMODERATION"));
    globalSettingsDto.setStatisticsIsPublic(globalSettings.get("STATISTICS_IS_PUBLIC"));
    return globalSettingsDto;
  }
}
