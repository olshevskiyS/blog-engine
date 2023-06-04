package ru.olshevskiy.blogengine.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.olshevskiy.blogengine.model.PostVote;

/**
 * PostVoteRepository.
 *
 * @author Sergey Olshevskiy
 */
@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, Integer> {

  Optional<PostVote> findByPostIdAndUserId(int postId, int userId);
}