package ru.olshevskiy.blogengine.service;

import ru.olshevskiy.blogengine.model.dto.response.TagsByQueryRs;

/**
 * TagService.
 *
 * @author Sergey Olshevskiy
 */
public interface TagService {

  TagsByQueryRs getTagsByQuery(String query);
}
