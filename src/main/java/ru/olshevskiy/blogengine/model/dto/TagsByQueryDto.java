package ru.olshevskiy.blogengine.model.dto;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * TagsByQueryDto.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
public class TagsByQueryDto {

  List<TagDto> tags;
}
