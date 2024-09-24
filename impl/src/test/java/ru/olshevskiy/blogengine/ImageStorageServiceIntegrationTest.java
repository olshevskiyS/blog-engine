package ru.olshevskiy.blogengine;

import static org.assertj.core.api.Assertions.assertThat;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.utils.StringUtils;
import jakarta.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * ImageStorageServiceIntegrationTest.
 *
 * @author Sergey Olshevskiy
 */
@SpringBootTest
public class ImageStorageServiceIntegrationTest {

  @Resource
  private Cloudinary cloudinary;

  private final File file = loadTestFileFromResourcesFolder();

  @BeforeEach
  void init() {
    if (StringUtils.isBlank(cloudinary.config.apiSecret)) {
      throw new IllegalArgumentException("Не настроен URL-адрес cloudinary");
    }
  }

  @Test
  void testUploadAndDestroyImageFile() throws IOException {
    Map uploadImageResult = cloudinary.uploader().upload(file,
                 ObjectUtils.asMap("public_id", "test/testImage", "transformation",
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
    String basePath = new File("").getAbsolutePath();
    String targetPath = basePath.concat("\\src\\test\\resources\\testImage.jpg");
    return new File(targetPath);
  }
}