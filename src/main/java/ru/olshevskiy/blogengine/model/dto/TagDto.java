package ru.olshevskiy.blogengine.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TagDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
@AllArgsConstructor
public class TagDto {

  String name;
  String weight;
}
