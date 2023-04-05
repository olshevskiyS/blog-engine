package ru.olshevskiy.blogengine.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ImageStorageConfig.
 *
 * @author Sergey Olshevskiy
 */
@Configuration
public class ImageStorageConfig {

  @Value("${cloudinary.url}")
  private String url;

  @Bean
  public Cloudinary cloudinaryServer() {
    return new Cloudinary(url);
  }
}
