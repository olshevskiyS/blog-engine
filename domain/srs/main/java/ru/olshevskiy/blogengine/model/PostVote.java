package ru.olshevskiy.blogengine.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
import lombok.experimental.Accessors;

/**
 * PostVote.
 *
 * @author Sergey Olshevskiy
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Accessors(chain = true)
@Table(name = "post_votes")
public class PostVote {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
  private int userId;

  @Column(name = "post_id", nullable = false, insertable = false, updatable = false)
  private int postId;

  @Column(name = "time", nullable = false)
  private LocalDateTime time;

  @Column(name = "value", nullable = false)
  private byte value;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "post_id")
  private Post post;

  /**
   * New postVote constructor.
   */
  public PostVote(int userId, int postId, byte value) {
    this.userId = userId;
    this.postId = postId;
    this.value = value;
    time = LocalDateTime.now(ZoneId.ofOffset("UTC", ZoneOffset.UTC));
  }
}