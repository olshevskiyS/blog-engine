package ru.olshevskiy.blogengine.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.olshevskiy.blogengine.dto.ImageExtension;
import ru.olshevskiy.blogengine.exception.ex.InvalidImageExtensionException;
import ru.olshevskiy.blogengine.model.User;

/**
 * ImageStorageService.
 *
 * @author Sergey Olshevskiy
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImageStorageService {

  private final Cloudinary cloudinary;

  @Value("${cloudinary.transformation.width-image}")
  private int widthImage;

  @Value("${cloudinary.transformation.height-image}")
  private int heightImage;

  @Value("${cloudinary.download-folder}")
  private String folderName;

  /**
   * ImageStorageService. Upload image method.
   */
  public void uploadImage(MultipartFile image, User currentUser) {
    log.info("Start upload image into the storage");
    checkInputFile(image);
    String fileName = Instant.now().getEpochSecond() + "-image-id" + currentUser.getId();
    String publicId = folderName + fileName;
    File file;
    String url;
    try {
      file = convertToFile(image);
      url = uploadToCloudinaryServer(file, publicId);
      log.info("The image has been successfully upload into the storage");
    } catch (IOException ex) {
      log.warn("Unable to upload image for reasons: " + ex);
      throw new RuntimeException(ex);
    }
    currentUser.setPhoto(url);
    deleteImageFileAfterUpload(file);
  }

  private void checkInputFile(MultipartFile image) {
    if (!ImageExtension.checkImageExtensionValue(StringUtils.getFilenameExtension(
            image.getOriginalFilename()))) {
      log.info("Invalid image extension. Finish request editProfile with exception.");
      throw new InvalidImageExtensionException();
    }
  }

  private File convertToFile(MultipartFile image) throws IOException {
    File file = new File(Objects.requireNonNull(image.getOriginalFilename()));
    FileOutputStream stream = new FileOutputStream(file);
    stream.write(image.getBytes());
    stream.close();
    return file;
  }

  private String uploadToCloudinaryServer(File image, String publicId) throws IOException {
    Map cloudinaryUrl = cloudinary.uploader()
            .upload(image, ObjectUtils.asMap("public_id", publicId, "transformation",
                    new Transformation<>().width(widthImage)
                                          .height(heightImage)
                                          .crop("fit")
                                          .fetchFormat("jpg")));
    return (String) cloudinaryUrl.get("url");
  }

  private void deleteImageFileAfterUpload(File image) {
    boolean isDeleted = image.delete();
    if (isDeleted) {
      log.info("The file has been successfully deleted from the application's file system");
    } else {
      log.warn("The file doesn't exist");
    }
  }

  /**
   * ImageStorageService. Delete image method.
   */
  public void deleteImage(String url) {
    log.info("Start delete image from the storage");
    String publicId = folderName + url.substring(url.lastIndexOf('/') + 1).split("\\.", 3)[0];
    try {
      cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("invalidate", true));
      log.info("The image has been successfully deleted from the storage");
    } catch (IOException ex) {
      log.warn("Unable to delete image for reasons: " + ex);
      throw new RuntimeException(ex);
    }
  }
}