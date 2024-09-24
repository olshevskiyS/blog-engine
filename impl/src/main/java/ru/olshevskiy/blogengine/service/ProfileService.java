package ru.olshevskiy.blogengine.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.olshevskiy.blogengine.dto.request.EditProfileWithPhotoRq;
import ru.olshevskiy.blogengine.dto.request.EditProfileWithoutPhotoRq;
import ru.olshevskiy.blogengine.dto.response.EditProfileRs;
import ru.olshevskiy.blogengine.exception.ex.EmailDuplicateException;
import ru.olshevskiy.blogengine.model.User;
import ru.olshevskiy.blogengine.repository.UserRepository;
import ru.olshevskiy.blogengine.security.SecurityUtils;

/**
 * ProfileService.
 *
 * @author Sergey Olshevskiy
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

  private final UserRepository userRepository;
  private final ImageStorageService imageStorageService;
  private final PasswordEncoder passwordEncoder;

  /**
   * ProfileService. Edit the current user's profile method (with upload image).
   */
  public EditProfileRs editProfileWithPhoto(EditProfileWithPhotoRq editProfileRq) {
    log.info("Start request editProfileWithPhoto");
    User currentUser = SecurityUtils.getCurrentUser();
    checkAndUpdateEmail(editProfileRq.getEmail(), currentUser);
    updateName(editProfileRq.getName(), currentUser);
    updatePassword(editProfileRq.getPassword(), currentUser);
    updateIfNewPhoto(editProfileRq.getPhoto(), editProfileRq.getRemovePhoto(), currentUser);
    User updatedCurrentUser = userRepository.save(currentUser);
    log.info("Finish request editProfileWithPhoto, user id is {}. {}",
            updatedCurrentUser.getId(), editProfileRq);
    return new EditProfileRs().setResult(true);
  }

  /**
   * ProfileService. Edit the current user's profile method (without upload image).
   */
  public EditProfileRs editProfileWithoutPhoto(EditProfileWithoutPhotoRq editProfileRq) {
    log.info("Start request editProfileWithoutPhoto");
    User currentUser = SecurityUtils.getCurrentUser();
    checkAndUpdateEmail(editProfileRq.getEmail(), currentUser);
    updateName(editProfileRq.getName(), currentUser);
    updatePassword(editProfileRq.getPassword(), currentUser);
    deletePhotoIfNecessary(editProfileRq.getRemovePhoto(), currentUser);
    User updatedCurrentUser = userRepository.save(currentUser);
    log.info("Finish request editProfileWithoutPhoto, user id is {}. {}",
            updatedCurrentUser.getId(), editProfileRq);
    return new EditProfileRs().setResult(true);
  }

  private void checkAndUpdateEmail(String newEmail, User currentUser) {
    if (newEmail != null) {
      Optional<User> optionalUser = userRepository.findByEmail(newEmail);
      if (optionalUser.isPresent() && !newEmail.equals(currentUser.getEmail())) {
        log.info("Email is duplicate. Finish request editProfile with exception.");
        throw new EmailDuplicateException();
      }
      if (optionalUser.isEmpty() && !newEmail.equals(currentUser.getEmail())) {
        currentUser.setEmail(newEmail);
      }
    }
  }

  private void updateName(String newName, User currentUser) {
    if (newName != null) {
      if (newName.equals(currentUser.getEmail())) {
        return;
      }
      currentUser.setName(newName);
    }
  }

  private void updatePassword(String newPassword, User currentUser) {
    if (newPassword != null) {
      if (passwordEncoder.matches(newPassword, currentUser.getPassword())) {
        return;
      }
      currentUser.setPassword(passwordEncoder.encode(newPassword));
    }
  }

  private void updateIfNewPhoto(MultipartFile photo, int photoDeletionMarker, User currentUser) {
    if (photo != null && photoDeletionMarker == 0) {
      imageStorageService.uploadImage(photo, currentUser);
    }
  }

  private void deletePhotoIfNecessary(int photoDeletionMarker, User currentUser) {
    if (photoDeletionMarker == 1 && currentUser.getPhoto() != null) {
      imageStorageService.deleteImage(currentUser.getPhoto());
      currentUser.setPhoto(null);
    }
  }
}