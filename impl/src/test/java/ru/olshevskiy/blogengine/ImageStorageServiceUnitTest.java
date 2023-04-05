package ru.olshevskiy.blogengine;

import static org.assertj.core.api.Assertions.assertThat;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.utils.StringUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * ImageStorageServiceUnitTest.
 *
 * @author Sergey Olshevskiy
 */
public class ImageStorageServiceUnitTest {

  private static Cloudinary cloudinary;
  private static final Properties prop;
  private static final String cloudinaryUrl;

  private final File file = loadTestFileFromResourcesFolder();

  static {
    prop = new Properties();
    try (InputStream stream = ImageStorageServiceUnitTest.class
            .getClassLoader().getResourceAsStream("application-test.yml")) {
      if (stream == null) {
        throw new FileNotFoundException();
      }
      prop.load(stream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    cloudinaryUrl = prop.getProperty("cloudinary-url");
  }

  @BeforeAll
  static void init() {
    if (StringUtils.isBlank(cloudinaryUrl)) {
      throw new IllegalArgumentException("Не настроен URL-адрес cloudinary");
    }
    if (!cloudinaryUrl.startsWith("cloudinary://")) {
      throw new IllegalArgumentException("Некорректный URL-адрес cloudinary");
    }
    cloudinary = new Cloudinary(cloudinaryUrl);
  }

  @Test
  void testUploadAndDestroyImageFile() throws IOException {
    if (cloudinary.config.apiSecret == null) {
      return;
    }
    String folderName = prop.getProperty("cloudinary-download-folder");
    Map uploadImageResult = cloudinary.uploader().upload(file,
                 ObjectUtils.asMap("public_id", folderName + "testImage", "transformation",
                   new Transformation<>().width(100)
                                         .height(100)
                                         .crop("fit")
                                         .fetchFormat("jpg")));
    assertThat(uploadImageResult.get("width")).isEqualTo(100);
    assertThat(uploadImageResult.get("height")).isEqualTo(100);
    Map<String, Object> paramsToSign = new HashMap<>();
    paramsToSign.put("public_id", uploadImageResult.get("public_id"));
    paramsToSign.put("version", ObjectUtils.asString(uploadImageResult.get("version")));
    String expectedSignature = cloudinary.apiSignRequest(paramsToSign, cloudinary.config.apiSecret);
    assertThat(expectedSignature).isEqualTo(uploadImageResult.get("signature"));

    Map deleteImageResult = cloudinary.uploader()
            .destroy((String) uploadImageResult.get("public_id"),
                     ObjectUtils.asMap("invalidate", true));
    assertThat(deleteImageResult.get("result")).isEqualTo("ok");
  }

  private File loadTestFileFromResourcesFolder() {
    return new File(Objects.requireNonNull(this.getClass().getClassLoader()
                    .getResource("testImage.jpg")).getFile());
  }
}