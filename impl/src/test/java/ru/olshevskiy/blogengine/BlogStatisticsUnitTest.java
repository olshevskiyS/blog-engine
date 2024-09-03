package ru.olshevskiy.blogengine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.olshevskiy.blogengine.dto.response.AllStatisticsRs;
import ru.olshevskiy.blogengine.dto.response.MyStatisticsRs;
import ru.olshevskiy.blogengine.exception.ex.StatisticsIsPublicException;
import ru.olshevskiy.blogengine.model.GlobalSetting;
import ru.olshevskiy.blogengine.model.ModerationStatus;
import ru.olshevskiy.blogengine.model.Post;
import ru.olshevskiy.blogengine.model.User;
import ru.olshevskiy.blogengine.projection.PostView;
import ru.olshevskiy.blogengine.repository.GlobalSettingRepository;
import ru.olshevskiy.blogengine.repository.PostRepository;
import ru.olshevskiy.blogengine.security.SecurityUtils;
import ru.olshevskiy.blogengine.service.GeneralService;

/**
 * BlogStatisticsUnitTest.
 *
 * @author Sergey Olshevskiy
 */
@ExtendWith(MockitoExtension.class)
public class BlogStatisticsUnitTest {

  @InjectMocks
  private GeneralService generalService;
  @Mock
  private PostRepository postRepository;
  @Mock
  private GlobalSettingRepository globalSettingRepository;

  private final MockedStatic<SecurityUtils> securityUtils = Mockito.mockStatic(SecurityUtils.class);

  private final User testUser = new User();
  private final PostView mockPostView1 = mock(PostView.class);
  private final PostView mockPostView2 = mock(PostView.class);
  private final PostView mockPostView3 = mock(PostView.class);
  private final LocalDateTime testDate1 = LocalDateTime.of(2023, Month.APRIL, 24, 14, 33, 45);
  private final LocalDateTime testDate2 = LocalDateTime.of(2023, Month.JANUARY, 10, 11, 15, 55);

  @BeforeEach
  void setMockOutputGeneral() {
    Post post1 = new Post()
            .setId(1)
            .setIsActive((byte) 1)
            .setModerationStatus(ModerationStatus.ACCEPTED)
            .setTime(testDate1)
            .setUserId(1)
            .setViewCount(5);
    when(mockPostView1.getPost()).thenReturn(post1);
    when(mockPostView1.getDislikeCount()).thenReturn(1);
    when(mockPostView1.getLikeCount()).thenReturn(2);

    Post post2 = new Post()
            .setId(2)
            .setIsActive((byte) 1)
            .setModerationStatus(ModerationStatus.ACCEPTED)
            .setTime(LocalDateTime.of(2023, Month.AUGUST, 12, 20, 22, 13))
            .setUserId(1)
            .setViewCount(6);
    when(mockPostView2.getPost()).thenReturn(post2);
    when(mockPostView2.getDislikeCount()).thenReturn(1);
    when(mockPostView2.getLikeCount()).thenReturn(1);

    Post post3 = new Post()
            .setId(3)
            .setIsActive((byte) 1)
            .setModerationStatus(ModerationStatus.ACCEPTED)
            .setTime(testDate2)
            .setUserId(2)
            .setViewCount(10);
    when(mockPostView3.getPost()).thenReturn(post3);
    when(mockPostView3.getDislikeCount()).thenReturn(3);
    when(mockPostView3.getLikeCount()).thenReturn(2);
  }

  @AfterEach
  void close() {
    securityUtils.close();
  }

  void setMockOutputForGetMyStatisticsTest() {
    setTestUserParameters((byte) 0);
    List<PostView> myPosts = Arrays.asList(mockPostView1, mockPostView2);
    when(postRepository.getAllMyActivePosts(testUser.getId())).thenReturn(myPosts);
  }

  void setMockOutputForGetAllStatisticsIfStatisticsAvailableAndRegularUserTest() {
    setTestUserParameters((byte) 0);
    setSettingAvailabilityOfStatistics("YES");
    List<PostView> posts = Arrays.asList(mockPostView1, mockPostView2, mockPostView3);
    when(postRepository.getAllPosts()).thenReturn(posts);
  }

  void setMockOutputForGetAllStatisticsIfStatisticsIsMotAvailableAndRegularUserTest() {
    setTestUserParameters((byte) 0);
    setSettingAvailabilityOfStatistics("NO");
  }

  void setMockOutputForGetAllStatisticsIfStatisticsIsMotAvailableAndModeratorTest() {
    setTestUserParameters((byte) 1);
    setSettingAvailabilityOfStatistics("NO");
    List<PostView> posts = Arrays.asList(mockPostView1, mockPostView2, mockPostView3);
    when(postRepository.getAllPosts()).thenReturn(posts);
  }

  private void setTestUserParameters(byte userOrModerator) {
    testUser.setId(1)
            .setIsModerator(userOrModerator);
    securityUtils.when(SecurityUtils::getCurrentUser).thenReturn(testUser);
  }

  private void setSettingAvailabilityOfStatistics(String availableOrNot) {
    GlobalSetting globalSetting = new GlobalSetting();
    globalSetting.setValue(availableOrNot);
    when(globalSettingRepository.findByCode("STATISTICS_IS_PUBLIC")).thenReturn(globalSetting);
  }

  @Test
  void testGetMyStatistics() {
    setMockOutputForGetMyStatisticsTest();

    MyStatisticsRs assignedMyStatisticsRs = new MyStatisticsRs();
    assignedMyStatisticsRs.setDislikesCount(2)
                          .setLikesCount(3)
                          .setPostsCount(2)
                          .setViewsCount(11)
                          .setFirstPublication(testDate1.toEpochSecond(ZoneOffset.UTC));

    MyStatisticsRs expectedMyStatisticsRs = generalService.getMyStatistics();
    assertThat(expectedMyStatisticsRs.getDislikesCount())
               .isEqualTo(assignedMyStatisticsRs.getDislikesCount());
    assertThat(expectedMyStatisticsRs.getLikesCount())
               .isEqualTo(assignedMyStatisticsRs.getLikesCount());
    assertThat(expectedMyStatisticsRs.getPostsCount())
               .isEqualTo(assignedMyStatisticsRs.getPostsCount());
    assertThat(expectedMyStatisticsRs.getViewsCount())
               .isEqualTo(assignedMyStatisticsRs.getViewsCount());
    assertThat(expectedMyStatisticsRs.getFirstPublication())
            .isEqualTo(assignedMyStatisticsRs.getFirstPublication());
  }

  @Test
  void testGetAllStatisticsIfStatisticsAvailableAndRegularUser() {
    setMockOutputForGetAllStatisticsIfStatisticsAvailableAndRegularUserTest();
    checkAssignedAndExpectedParametersOfAllStatistics();
  }

  @Test
  void testGetAllStatisticsIfStatisticsIsNotAvailableAndModerator() {
    setMockOutputForGetAllStatisticsIfStatisticsIsMotAvailableAndModeratorTest();
    checkAssignedAndExpectedParametersOfAllStatistics();
  }

  private void checkAssignedAndExpectedParametersOfAllStatistics() {
    AllStatisticsRs assignedAllStatisticsRs = new AllStatisticsRs();
    assignedAllStatisticsRs.setDislikesCount(5)
            .setLikesCount(5)
            .setPostsCount(3)
            .setViewsCount(21)
            .setFirstPublication(testDate2.toEpochSecond(ZoneOffset.UTC));

    AllStatisticsRs expectedAllStatisticsRs = generalService.getAllStatistics();
    assertThat(expectedAllStatisticsRs.getDislikesCount())
            .isEqualTo(assignedAllStatisticsRs.getDislikesCount());
    assertThat(expectedAllStatisticsRs.getLikesCount())
            .isEqualTo(assignedAllStatisticsRs.getLikesCount());
    assertThat(expectedAllStatisticsRs.getPostsCount())
            .isEqualTo(assignedAllStatisticsRs.getPostsCount());
    assertThat(expectedAllStatisticsRs.getViewsCount())
            .isEqualTo(assignedAllStatisticsRs.getViewsCount());
    assertThat(expectedAllStatisticsRs.getFirstPublication())
            .isEqualTo(assignedAllStatisticsRs.getFirstPublication());
  }

  @Test
  void testGetAllStatisticsIfStatisticsIsNotAvailableAndRegularUser() {
    setMockOutputForGetAllStatisticsIfStatisticsIsMotAvailableAndRegularUserTest();
    assertThrows(StatisticsIsPublicException.class, () -> generalService.getAllStatistics());
  }
}