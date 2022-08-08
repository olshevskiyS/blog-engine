package ru.olshevskiy.blogengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.olshevskiy.blogengine.model.entity.User;

/**
 * UserRepository.
 *
 * @author Sergey Olshevskiy
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
