package ru.olshevskiy.blogengine.service;

import ru.olshevskiy.blogengine.model.dto.GlobalSettingDto;

/**
 * Интерфейс сервиса глобальных настроек блога.
 */
public interface GlobalSettingService {

  GlobalSettingDto getGlobalSettings();
}
