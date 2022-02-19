package ru.olshevskiy.blogengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.olshevskiy.blogengine.model.entity.Tag;

/**
 * Репозиторий для работы с тегами постов блога.
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
}