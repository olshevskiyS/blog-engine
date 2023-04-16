package ru.olshevskiy.blogengine.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.olshevskiy.blogengine.model.PostComment;

/**
 * PostCommentRepository.
 *
 * @author Sergey Olshevskiy
 */
@Repository
public interface PostCommentRepository extends CrudRepository<PostComment, Integer> {
}
