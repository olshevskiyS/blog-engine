package ru.olshevskiy.blogengine.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.olshevskiy.blogengine.model.User;

/**
 * UserRepository.
 *
 * @author Sergey Olshevskiy
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  List<User> findAllByIsModeratorEquals(byte isModerator);
}
