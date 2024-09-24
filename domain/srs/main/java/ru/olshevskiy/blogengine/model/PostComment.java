package ru.olshevskiy.blogengine.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

/**
 * PostComment.
 *
 * @author Sergey Olshevskiy
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "post_comments")
public class PostComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "parent_id", insertable = false, updatable = false)
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
  @ToString.Exclude
  private User user;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "post_id")
  @ToString.Exclude
  private Post post;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "parent_id")
  @ToString.Exclude
  private PostComment parentComment;

  @OneToMany(mappedBy = "parentComment", fetch = FetchType.LAZY, orphanRemoval = true)
  @ToString.Exclude
  private Set<PostComment> childrenPosts = new HashSet<>();

  /**
   * New comment for post constructor.
   */
  public PostComment(int userId, int postId, String text) {
    this.userId = userId;
    this.postId = postId;
    this.text = text;
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
    PostComment comparedPostComment = (PostComment) that;
    return getId() != null && Objects.equals(getId(), comparedPostComment.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
            : getClass().hashCode();
  }
}