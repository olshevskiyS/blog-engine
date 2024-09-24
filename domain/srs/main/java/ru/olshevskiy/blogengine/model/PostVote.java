package ru.olshevskiy.blogengine.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.proxy.HibernateProxy;

/**
 * PostVote.
 *
 * @author Sergey Olshevskiy
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@Entity
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
  @ToString.Exclude
  private User user;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "post_id")
  @ToString.Exclude
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

  @Override
  public final boolean equals(Object that) {
    if (this == that) {
      return true;
    }
    if (that == null) {
      return false;
    }
    Class<?> thatEffectiveClass = that instanceof HibernateProxy
            ? ((HibernateProxy) that).getHibernateLazyInitializer().getPersistentClass()
            : that.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
    if (thisEffectiveClass != thatEffectiveClass) {
      return false;
    }
    PostVote comparedPostVote = (PostVote) that;
    return getId() != null && Objects.equals(getId(), comparedPostVote.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
            : getClass().hashCode();
  }
}