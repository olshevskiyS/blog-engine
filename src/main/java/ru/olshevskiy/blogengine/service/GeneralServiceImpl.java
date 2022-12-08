package ru.olshevskiy.blogengine.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.olshevskiy.blogengine.model.dto.response.CalendarRs;
import ru.olshevskiy.blogengine.model.dto.response.GlobalSettingsRs;
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
  public GlobalSettingsRs getGlobalSettings() {
    log.info("Start request getGlobalSettings");
    GlobalSettingsRs globalSettingsRs = new GlobalSettingsRs();
    Map<String, Boolean> globalSettings = new HashMap<>();
    globalSettingRepository.findAll().forEach(setting -> {
      boolean value;
      value = setting.getValue().equals("YES");
      globalSettings.put(setting.getCode(), value);
    });
    globalSettingsRs.setMultiuserMode(globalSettings.get("MULTIUSER_MODE"))
            .setPostPremoderation(globalSettings.get("POST_PREMODERATION"))
            .setStatisticsIsPublic(globalSettings.get("STATISTICS_IS_PUBLIC"));
    log.info("Finish request getGlobalSettings");
    return globalSettingsRs;
  }

  @Override
  public CalendarRs getCalendar(String year) {
    log.info("Start request getCalendar with year = " + year);
    if (year == null || year.isEmpty()) {
      year = String.valueOf(LocalDate.now().getYear());
    }
    CalendarRs calendarRs = new CalendarRs();
    List<Integer> years = calendarRepository.getYearsAllActivePosts();
    Map<String, Long> postsByYear = calendarRepository.getPostsByYear(year);
    calendarRs.setYears(years)
              .setPosts(postsByYear);
    log.info("Finish request getCalendar");
    return calendarRs;
  }
}
