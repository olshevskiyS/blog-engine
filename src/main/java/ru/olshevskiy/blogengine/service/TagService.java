package ru.olshevskiy.blogengine.service;

import ru.olshevskiy.blogengine.model.dto.TagsByQueryDto;

/**
 * TagService.
 *
 * @author Sergey Olshevskiy
 */
public interface TagService {

  TagsByQueryDto getTagsByQuery(String query);
}
