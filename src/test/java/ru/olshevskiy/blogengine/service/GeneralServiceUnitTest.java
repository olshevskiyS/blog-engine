package ru.olshevskiy.blogengine.service;

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
import ru.olshevskiy.blogengine.model.dto.response.GlobalSettingsRs;
import ru.olshevskiy.blogengine.model.entity.GlobalSetting;
import ru.olshevskiy.blogengine.repository.GlobalSettingRepository;

/**
 * GeneralServiceUnitTest.
 *
 * @author Sergey Olshevskiy
 */
@ExtendWith(MockitoExtension.class)
public class GeneralServiceUnitTest {

  @InjectMocks
  private GeneralServiceImpl generalService;

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
    GlobalSettingsRs assignedGlobalSettingRs = new GlobalSettingsRs();
    assignedGlobalSettingRs.setMultiuserMode(true)
            .setPostPremoderation(false)
            .setStatisticsIsPublic(false);
    GlobalSettingsRs expectedGlobalSettingRs = generalService.getGlobalSettings();
    assertThat(expectedGlobalSettingRs).isEqualTo(assignedGlobalSettingRs);
  }
}
