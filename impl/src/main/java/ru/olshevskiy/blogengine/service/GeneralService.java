package ru.olshevskiy.blogengine.service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.olshevskiy.blogengine.dto.response.AllStatisticsRs;
import ru.olshevskiy.blogengine.dto.response.CalendarRs;
import ru.olshevskiy.blogengine.dto.response.GlobalSettingsRs;
import ru.olshevskiy.blogengine.dto.response.MyStatisticsRs;
import ru.olshevskiy.blogengine.dto.response.StatisticsRs;
import ru.olshevskiy.blogengine.exception.ex.StatisticsIsPublicException;
import ru.olshevskiy.blogengine.model.GlobalSetting;
import ru.olshevskiy.blogengine.model.User;
import ru.olshevskiy.blogengine.projection.PostView;
import ru.olshevskiy.blogengine.repository.CalendarRepository;
import ru.olshevskiy.blogengine.repository.GlobalSettingRepository;
import ru.olshevskiy.blogengine.repository.PostRepository;
import ru.olshevskiy.blogengine.security.SecurityUtils;

/**
 * GeneralService.
 *
 * @author Sergey Olshevskiy
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeneralService {

  private final GlobalSettingRepository globalSettingRepository;
  private final CalendarRepository calendarRepository;
  private final PostRepository postRepository;

  /**
   * GeneralService. Getting global settings method.
   */
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

  /**
   * GeneralService. Getting a calendar method.
   */
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

  /**
   * GeneralService. Getting statistics for the current user method.
   */
  public MyStatisticsRs getMyStatistics() {
    log.info("Start request getMyStatistics");
    User currentUser = SecurityUtils.getCurrentUser();
    List<PostView> myPosts = postRepository.getAllMyActivePosts(currentUser.getId());
    myPosts.sort(Comparator.comparing(p -> p.getPost().getTime()));
    MyStatisticsRs myStatistics = new MyStatisticsRs();
    collectStatistics(myStatistics, myPosts);
    log.info("Finish request getMyStatistics");
    return myStatistics;
  }

  /**
   * GeneralService. Getting statistics for the entire blog method.
   */
  public AllStatisticsRs getAllStatistics() {
    log.info("Start request getAllStatistics");
    checkAvailabilityOfPublicStatistics();
    List<PostView> posts = postRepository.getAllPosts();
    posts.sort(Comparator.comparing(p -> p.getPost().getTime()));
    AllStatisticsRs blogStatistics = new AllStatisticsRs();
    collectStatistics(blogStatistics, posts);
    log.info("Finish request getAllStatistics");
    return blogStatistics;
  }

  private void checkAvailabilityOfPublicStatistics() {
    User currentUser = SecurityUtils.getCurrentUser();
    GlobalSetting globalSetting = globalSettingRepository.findByCode("STATISTICS_IS_PUBLIC");
    if (currentUser.getIsModerator() == 0 && globalSetting.getValue().equals("NO")) {
      log.info("Statistics are not available. Finish request getAllStatistics with exception");
      throw new StatisticsIsPublicException();
    }
  }

  private void collectStatistics(StatisticsRs statisticsRs, List<PostView> posts) {
    int myLikesCount = 0;
    int myDislikesCount = 0;
    int myViewsCount = 0;
    for (PostView postView : posts) {
      myLikesCount += postView.getLikeCount();
      myDislikesCount += postView.getDislikeCount();
      myViewsCount += postView.getPost().getViewCount();
    }
    statisticsRs.setPostsCount(posts.size())
                .setLikesCount(myLikesCount)
                .setDislikesCount(myDislikesCount)
                .setViewsCount(myViewsCount)
                .setFirstPublication(posts.get(0).getPost().getTime()
                        .toEpochSecond(ZoneOffset.UTC));
  }
}