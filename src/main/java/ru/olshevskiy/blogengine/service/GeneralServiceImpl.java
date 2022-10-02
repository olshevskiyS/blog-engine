package ru.olshevskiy.blogengine.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.olshevskiy.blogengine.model.dto.CalendarDto;
import ru.olshevskiy.blogengine.model.dto.GlobalSettingDto;
import ru.olshevskiy.blogengine.repository.CalendarRepository;
import ru.olshevskiy.blogengine.repository.GlobalSettingRepository;

/**
 * GeneralServiceImpl.
 *
 * @author Sergey Olshevskiy
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeneralServiceImpl implements GeneralService {

  private final GlobalSettingRepository globalSettingRepository;
  private final CalendarRepository calendarRepository;

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

  @Override
  public CalendarDto getCalendar(String year) {
    log.info("Start request getCalendar with year = " + year);
    CalendarDto calendarDto = new CalendarDto();
    List<Integer> years = calendarRepository.getYearsAllActivePosts();
    Map<String, Long> postsByYear = calendarRepository.getPostsByYear(year);
    calendarDto.setYears(years)
               .setPosts(postsByYear);
    log.info("Finish request getCalendar");
    return calendarDto;
  }
}
