package ru.olshevskiy.blogengine.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

  String selectPosts = "SELECT p AS post, "
          + "SUM(CASE WHEN v.value > 0 THEN 1 ELSE 0 END) AS likeCount, "
          + "SUM(CASE WHEN v.value < 0 THEN 1 ELSE 0 END) AS dislikeCount "
          + "FROM Post p ";

  String primaryCriterion = "WHERE p.isActive = 1 "
          + "AND p.moderationStatus = 'ACCEPTED' "
          + "AND p.time <= now()";

  @Query(value = ("SELECT count(*) FROM Post p " + primaryCriterion))
  int getCountActivePosts();

  @Query(value = (selectPosts + "LEFT JOIN p.votes v " + primaryCriterion + " GROUP BY p"))
  Page<PostView> getActivePosts(Pageable pageable);

  @Query(value = (selectPosts + "LEFT JOIN p.votes v " + primaryCriterion
          + " AND p.text LIKE CONCAT('%', :query, '%') GROUP BY p"))
  Page<PostView> getActivePostsByQuery(@Param("query") String query, Pageable pageable);

  @Query(value = (selectPosts + "LEFT JOIN p.votes v " + primaryCriterion
          + " AND DATE_FORMAT(p.time, '%Y-%m-%d') LIKE CONCAT('%', :date, '%') GROUP BY p"))
  Page<PostView> getActivePostsByDate(@Param("date") String date, Pageable pageable);

  @Query(value = (selectPosts + "JOIN p.tags t LEFT JOIN p.votes v "  + primaryCriterion
          + " AND t.name LIKE CONCAT('%', :tag, '%') GROUP BY p"))
  Page<PostView> getActivePostsByTag(@Param("tag") String tag, Pageable pageable);

  @Query(value = (selectPosts + "LEFT JOIN p.votes v WHERE p.id = :id GROUP BY p"))
  Optional<PostView> getPostById(@Param("id") Integer id);
}