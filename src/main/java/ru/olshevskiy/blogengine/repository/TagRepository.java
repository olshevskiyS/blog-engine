package ru.olshevskiy.blogengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.olshevskiy.blogengine.model.entity.Tag;

/**
 * TagRepository.
 *
 * @author Sergey Olshevskiy
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
}