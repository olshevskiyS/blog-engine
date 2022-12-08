package ru.olshevskiy.blogengine.model.dto.response;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.olshevskiy.blogengine.model.dto.TagDto;

/**
 * TagsByQueryRs.
 *
 * @author Sergey Olshevskiy
 */
@Data
@Accessors(chain = true)
public class TagsByQueryRs {

  private List<TagDto> tags;
}
