package ru.olshevskiy.blogengine.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.proxy.HibernateProxy;

/**
 * Post.
 *
 * @author Sergey Olshevskiy
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Accessors(chain = true)
@Table(name = "posts")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "is_active", nullable = false)
  private byte isActive;

  @Enumerated(EnumType.STRING)
  @Column(name = "moderation_status", nullable = false)
  private ModerationStatus moderationStatus;

  @Column(name = "moderator_id", insertable = false, updatable = false)
  private Integer moderatorId;

  @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
  private int userId;

  @Column(name = "time", nullable = false)
  private LocalDateTime time;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "text", nullable = false, columnDefinition = "text")
  private String text;

  @Column(name = "view_count", nullable = false)
  private int viewCount;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  @ToString.Exclude
  private User user;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "moderator_id")
  @ToString.Exclude
  private User moderator;

  @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true)
  @ToString.Exclude
  private Set<PostVote> votes = new HashSet<>();

  @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true)
  @ToString.Exclude
  private Set<PostComment> comments = new HashSet<>();

  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST},
          mappedBy = "posts", fetch = FetchType.LAZY)
  @ToString.Exclude
  private Set<Tag> tags = new HashSet<>();

  /**
   * New post constructor.
   */
  public Post(String title, String text) {
    this.title = title;
    this.text = text;
    moderationStatus = ModerationStatus.NEW;
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
    Post comparedPost = (Post) that;
    return getId() != null && Objects.equals(getId(), comparedPost.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
            : getClass().hashCode();
  }
}