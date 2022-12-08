package ru.olshevskiy.blogengine.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import ru.olshevskiy.blogengine.model.entity.CaptchaCode;

/**
 * CaptchaRepository.
 *
 * @author Sergey Olshevskiy
 */
@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaCode, Integer> {

  @Modifying
  void deleteByTimeBefore(LocalDateTime time);

  Optional<CaptchaCode> findByCode(String code);
}