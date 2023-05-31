package ru.olshevskiy.blogengine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import ru.olshevskiy.blogengine.dto.request.EditProfileWithPhotoRq;
import ru.olshevskiy.blogengine.dto.request.EditProfileWithoutPhotoRq;
import ru.olshevskiy.blogengine.dto.response.EditProfileRs;
import ru.olshevskiy.blogengine.exception.ex.EmailDuplicateException;
import ru.olshevskiy.blogengine.model.User;
import ru.olshevskiy.blogengine.repository.UserRepository;
import ru.olshevskiy.blogengine.service.ImageStorageService;
import ru.olshevskiy.blogengine.service.ProfileService;
import ru.olshevskiy.blogengine.utils.TestImage;

/**
 * EditProfileIntegrationTest.
 *
 * @author Sergey Olshevskiy
 */
@SpringBootTest
@Transactional
@TestPropertySource(properties = "cloudinary.download-folder=test/")
public class EditProfileIntegrationTest extends BaseIntegrationTestWithTestContainer {

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @Autowired
  private ProfileService profileService;
  @Autowired
  private ImageStorageService imageStorageService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  private MockMultipartFile mockMultipartFile;

  private EditProfileWithPhotoRq testData1;
  private EditProfileWithoutPhotoRq testData2;
  private EditProfileWithoutPhotoRq testData3;
  private EditProfileWithoutPhotoRq testData4;

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
              .apply(SecurityMockMvcConfigurers.springSecurity())
              .build();

    mockMultipartFile = TestImage.getMockMultipartFile();

    testData1 = new EditProfileWithPhotoRq();
    testData1.setPhoto(mockMultipartFile)
             .setName("Some name")
             .setEmail("user011@email.com")
             .setPassword("1234567")
             .setRemovePhoto(0);

    testData2 = new EditProfileWithoutPhotoRq();
    testData2.setName("пользователь02")
             .setEmail("user02@email.com")
             .setPassword("2222222");

    testData3 = new EditProfileWithoutPhotoRq();
    testData3.setPhoto("")
             .setName("пользователь02")
             .setEmail("user02@email.com")
             .setRemovePhoto(1);

    testData4 = new EditProfileWithoutPhotoRq();
    testData4.setName("пользователь02")
             .setEmail("user01@email.com");
  }

  @Test
  @WithUserDetails(value = "user01@email.com",
                   userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testWhichEndpointIsCalledIfRequestContainsImage() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.multipart("/api/profile/my")
           .file(mockMultipartFile)
           .param("name", "пользователь01")
           .param("email", "user01@email.com")
           .param("removePhoto", "0")
           .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
           .andExpect(MockMvcResultMatchers.status().isOk())
           .andExpect(MockMvcResultMatchers.handler().methodName("editProfileWithPhoto"));

    imageStorageService.deleteImage(userRepository.getById(1).getPhoto());
  }

  @Test
  @WithUserDetails(value = "user01@email.com",
                   userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testWhichEndpointIsCalledIfRequestDoesNotContainImage() throws Exception {
    String jsonRequest = "{\"name\": \"пользователь01\", \"email\": \"user01@email.com\"}";
    mockMvc.perform(MockMvcRequestBuilders.post("/api/profile/my")
           .content(jsonRequest)
           .contentType(MediaType.APPLICATION_JSON_VALUE))
           .andExpect(MockMvcResultMatchers.status().isOk())
           .andExpect(MockMvcResultMatchers.handler().methodName("editProfileWithoutPhoto"));
  }

  @Test
  @WithUserDetails(value = "user01@email.com",
          userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testUpdatingProfileWithDataChanges() {
    User currentUserBeforeUpdate = userRepository.getById(1);
    assertThat(currentUserBeforeUpdate.getPhoto()).isNull();

    EditProfileRs editProfileRs = profileService.editProfileWithPhoto(testData1);
    assertThat(editProfileRs.isResult()).isTrue();

    Optional<User> currentUserAfterUpdate = userRepository.findByEmail(testData1.getEmail());
    assertThat(currentUserAfterUpdate.isPresent()).isTrue();
    assertThat(currentUserAfterUpdate.get().getName()).isEqualTo(testData1.getName());
    assertThat(passwordEncoder.matches(testData1.getPassword(),
               currentUserAfterUpdate.get().getPassword())).isTrue();
    assertThat(currentUserAfterUpdate.get().getPhoto()).isNotNull();

    imageStorageService.deleteImage(currentUserAfterUpdate.get().getPhoto());
  }

  @Test
  @WithUserDetails(value = "user02@email.com",
          userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testUpdatingProfileWithoutDataChanges() {
    Optional<User> currentUserBeforeUpdate = userRepository.findByEmail(testData2.getEmail());
    assertThat(currentUserBeforeUpdate.isPresent()).isTrue();

    EditProfileRs editProfileRs = profileService.editProfileWithoutPhoto(testData2);
    assertThat(editProfileRs.isResult()).isTrue();

    Optional<User> currentUserAfterUpdate = userRepository.findByEmail(testData2.getEmail());
    assertThat(currentUserAfterUpdate.isPresent()).isTrue();
    assertThat(currentUserAfterUpdate.get().getName())
               .isEqualTo(currentUserBeforeUpdate.get().getName());
    assertThat(currentUserAfterUpdate.get().getPassword())
               .isEqualTo(currentUserBeforeUpdate.get().getPassword());
  }

  @Test
  @WithUserDetails(value = "user02@email.com",
          userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testUpdatingProfileWithPhotoDeletion() {
    User currentUserBeforeUpdate = userRepository.getById(2);
    assertThat(currentUserBeforeUpdate.getPhoto()).isNotNull();

    EditProfileRs editProfileRs = profileService.editProfileWithoutPhoto(testData3);
    assertThat(editProfileRs.isResult()).isTrue();

    User currentUserAfterUpdate = userRepository.getById(2);
    assertThat(currentUserAfterUpdate.getPhoto()).isNull();
  }

  @Test
  @WithUserDetails(value = "user02@email.com",
          userDetailsServiceBeanName = "userDetailsServiceImpl")
  void testCheckVerificationOfDuplicateEmail() {
    assertThrows(EmailDuplicateException.class,
                 () -> profileService.editProfileWithoutPhoto(testData4));
  }
}