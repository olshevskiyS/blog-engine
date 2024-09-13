package ru.olshevskiy.blogengine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.olshevskiy.blogengine.dto.response.GetGlobalSettingsRs;
import ru.olshevskiy.blogengine.model.GlobalSetting;
import ru.olshevskiy.blogengine.repository.GlobalSettingRepository;
import ru.olshevskiy.blogengine.service.GeneralService;

/**
 * GetGlobalSettingsUnitTest.
 *
 * @author Sergey Olshevskiy
 */
@ExtendWith(MockitoExtension.class)
public class GetGlobalSettingsUnitTest {

  @InjectMocks
  private GeneralService generalService;
  @Mock
  private GlobalSettingRepository globalSettingRepository;

  @BeforeEach
  void setMockOutput() {
    List<GlobalSetting> globalSettings = new ArrayList<>();
    globalSettings.add(new GlobalSetting("MULTIUSER_MODE", "MM", "YES"));
    globalSettings.add(new GlobalSetting("POST_PREMODERATION", "PP", "NO"));
    globalSettings.add(new GlobalSetting("STATISTICS_IS_PUBLIC", "SIP", "NO"));
    when(globalSettingRepository.findAll()).thenReturn(globalSettings);
  }

  @Test
  void testGetGlobalSettings() {
    GetGlobalSettingsRs assignedGlobalSettingRs = new GetGlobalSettingsRs();
    assignedGlobalSettingRs.setMultiuserMode(true)
            .setPostPremoderation(false)
            .setStatisticsIsPublic(false);
    GetGlobalSettingsRs expectedGlobalSettingRs = generalService.getGlobalSettings();
    assertThat(expectedGlobalSettingRs).isEqualTo(assignedGlobalSettingRs);
  }
}