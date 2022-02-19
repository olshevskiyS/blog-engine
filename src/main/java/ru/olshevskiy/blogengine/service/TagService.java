package ru.olshevskiy.blogengine.service;

import java.util.List;
import java.util.Map;
import ru.olshevskiy.blogengine.model.dto.TagDto;

/**
 * Интерфейс сервиса взаимодействия с тегами постов блога.
 */
public interface TagService {

  Map<String, List<TagDto>> getTagsByQuery(String query);
}
