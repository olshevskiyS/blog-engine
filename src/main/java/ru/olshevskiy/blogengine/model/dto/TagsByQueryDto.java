package ru.olshevskiy.blogengine.model.dto;

import java.util.List;
import lombok.Data;

/**
 * TagsByQueryDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
public class TagsByQueryDto {

  List<TagDto> tags;
}
