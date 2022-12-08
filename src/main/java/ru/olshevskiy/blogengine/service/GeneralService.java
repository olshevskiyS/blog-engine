package ru.olshevskiy.blogengine.service;

import ru.olshevskiy.blogengine.model.dto.response.CalendarRs;
import ru.olshevskiy.blogengine.model.dto.response.GlobalSettingsRs;

/**
 * GlobalSettingService.
 *
 * @author Sergey Olshevskiy
 */
public interface GeneralService {

  GlobalSettingsRs getGlobalSettings();

  CalendarRs getCalendar(String year);
}
