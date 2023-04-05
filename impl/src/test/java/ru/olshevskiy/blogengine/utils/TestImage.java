package ru.olshevskiy.blogengine.utils;

import java.io.IOException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

/**
 * TestImage.
 *
 * @author Sergey Olshevskiy
 */
@Component
public class TestImage {

  private final ResourceLoader resourceLoader = new DefaultResourceLoader();

  private static MockMultipartFile multipartFile;

  {
    try {
      multipartFile = new MockMultipartFile("photo", "testImage.jpg",
             MediaType.MULTIPART_FORM_DATA_VALUE, loadImageViaResourceLoader().getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Resource loadImageViaResourceLoader() {
    return resourceLoader.getResource("classpath:testImage.jpg");
  }

  public static MockMultipartFile getMockMultipartFile() {
    return multipartFile;
  }
}