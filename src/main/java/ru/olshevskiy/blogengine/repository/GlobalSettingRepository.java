package ru.olshevskiy.blogengine.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.olshevskiy.blogengine.model.entity.GlobalSetting;

/**
 * Репозиторий для работы с глобальными настройками блога.
 */
@Repository
public interface GlobalSettingRepository extends CrudRepository<GlobalSetting, Integer> {

  @Override
  Iterable<GlobalSetting> findAll();
}
