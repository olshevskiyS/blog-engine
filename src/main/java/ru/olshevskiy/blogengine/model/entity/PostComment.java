package ru.olshevskiy.blogengine.model.entity;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс сущности комментариев постов.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "post_comments")
public class PostComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "parent_id")
  private Integer parentId;

  @Column(name = "post_id", nullable = false, insertable = false, updatable = false)
  private int postId;

  @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
  private int userId;

  @Column(name = "time", nullable = false)
  private LocalDateTime time;

  @Column(name = "text", nullable = false, columnDefinition = "text")
  private String text;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "post_id")
  private Post post;

  PostComment(int userId, int postId, String text) {
    this.userId = userId;
    this.postId = postId;
    this.text = text;
    time = LocalDateTime.now();
  }
}