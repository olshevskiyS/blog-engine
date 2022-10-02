package ru.olshevskiy.blogengine.service;

import ru.olshevskiy.blogengine.model.dto.CalendarDto;
import ru.olshevskiy.blogengine.model.dto.GlobalSettingDto;

/**
 * GlobalSettingService.
 *
 * @author Sergey Olshevskiy
 */
public interface GeneralService {

  GlobalSettingDto getGlobalSettings();

  CalendarDto getCalendar(String year);
}
