package ru.olshevskiy.blogengine;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.olshevskiy.blogengine.dto.request.SetGlobalSettingsRq;
import ru.olshevskiy.blogengine.repository.GlobalSettingRepository;
import ru.olshevskiy.blogengine.service.GeneralService;

/**
 * SetGlobalSettingsIntegrationTest.
 *
 * @author Sergey Olshevskiy
 */
@SpringBootTest
public class SetGlobalSettingsIntegrationTest extends BaseIntegrationTestWithTestContainer {

  @Autowired
  private GeneralService generalService;
  @Autowired
  private GlobalSettingRepository globalSettingRepository;

  private SetGlobalSettingsRq setGlobalSettingsRq;

  @BeforeEach
  void setUp() {
    setGlobalSettingsRq = new SetGlobalSettingsRq()
            .setMultiuserMode(false)
            .setPostPremoderation(true)
            .setStatisticsIsPublic(false);
  }

  @Test
  void testSetNewValuesOfGlobalSettings() {
    Map<String, String> currentSettings = new HashMap<>();
    checkValuesOfGlobalSettings(currentSettings, "YES", "YES");
    generalService.setGlobalSettings(setGlobalSettingsRq);
    Map<String, String> newSettings = new HashMap<>();
    checkValuesOfGlobalSettings(newSettings, "NO", "NO");
  }

  private void checkValuesOfGlobalSettings(Map<String, String> settings,
                                           String multiuserMode,
                                           String statisticsIsPublic) {
    globalSettingRepository.findAll().forEach(setting ->
            settings.put(setting.getCode(), setting.getValue()));
    assertThat(settings.get("MULTIUSER_MODE")).isEqualTo(multiuserMode);
    assertThat(settings.get("POST_PREMODERATION")).isEqualTo("YES");
    assertThat(settings.get("STATISTICS_IS_PUBLIC")).isEqualTo(statisticsIsPublic);
  }
}