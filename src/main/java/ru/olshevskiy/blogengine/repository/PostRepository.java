package ru.olshevskiy.blogengine.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.olshevskiy.blogengine.model.entity.Post;
import ru.olshevskiy.blogengine.model.projection.PostView;

/**
 * PostRepository.
 *
 * @author Sergey Olshevskiy
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

  @Query(value = "SELECT count(*) FROM Post p "
          + "WHERE p.isActive = 1 "
          + "AND p.moderationStatus = 'ACCEPTED' "
          + "AND p.time <= now()")
  int getCountAllActivePosts();

  @Query(value = "SELECT p AS post, "
          + "SUM(CASE WHEN v.value > 0 THEN 1 ELSE 0 END) AS likeCount, "
          + "SUM(CASE WHEN v.value < 0 THEN 1 ELSE 0 END) AS dislikeCount "
          + "FROM Post p "
          + "LEFT JOIN p.votes v "
          + "WHERE p.isActive = 1 "
          + "AND p.moderationStatus = 'ACCEPTED' "
          + "AND p.time <= now() "
          + "GROUP BY p")
  List<PostView> getAllActivePostsWithCommentsAndVotes(Pageable pageable);
}
