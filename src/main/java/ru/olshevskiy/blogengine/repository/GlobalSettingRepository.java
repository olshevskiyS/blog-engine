package ru.olshevskiy.blogengine.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.olshevskiy.blogengine.model.entity.GlobalSetting;

/**
 * GlobalSettingRepository.
 *
 * @author Sergey Olshevskiy
 */
@Repository
public interface GlobalSettingRepository extends CrudRepository<GlobalSetting, Integer> {
}
