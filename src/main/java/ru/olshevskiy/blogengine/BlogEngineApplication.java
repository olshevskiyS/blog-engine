package ru.olshevskiy.blogengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Точка входа в приложение.
 */
@SpringBootApplication
public class BlogEngineApplication {

  public static void main(String[] args) {
    SpringApplication.run(BlogEngineApplication.class, args);
  }
}