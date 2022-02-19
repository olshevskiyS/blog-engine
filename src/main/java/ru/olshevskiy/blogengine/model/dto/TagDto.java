package ru.olshevskiy.blogengine.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс DTO тегов для постов блога.
 */
@Data
@AllArgsConstructor
public class TagDto {

  String name;

  String weight;
}
