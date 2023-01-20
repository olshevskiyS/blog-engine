package ru.olshevskiy.blogengine.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.olshevskiy.blogengine.model.Tag;

/**
 * TagRepository.
 *
 * @author Sergey Olshevskiy
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

  Optional<Tag> findByName(String name);
}