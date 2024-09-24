package ru.olshevskiy.blogengine.dto;

import lombok.Getter;

/**
 * ImageExtension.
 *
 * @author Sergey Olshevskiy
 */
@Getter
public enum ImageExtension {

  JPG("jpg"), PNG("png");

  private final String extension;

  ImageExtension(String extension) {
    this.extension = extension;
  }

  /**
   * ImageExtension. Check extension's value of file
   */
  public static boolean checkImageExtensionValue(String value) {
    for (ImageExtension extension : values()) {
      if (extension.getExtension().equals(value)) {
        return true;
      }
    }
    return false;
  }
}