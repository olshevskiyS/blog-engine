package ru.olshevskiy.blogengine.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
import lombok.experimental.Accessors;
import org.hibernate.proxy.HibernateProxy;

/**
 * User.
 *
 * @author Sergey Olshevskiy
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "is_moderator", nullable = false)
  private byte isModerator;

  @Column(name = "reg_time", nullable = false)
  private LocalDateTime regTime;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "code")
  private String code;

  @Column(name = "code_create_time")
  private LocalDateTime codeCreateTime;

  @Column(name = "photo", columnDefinition = "text")
  private String photo;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
  @ToString.Exclude
  private Set<PostVote> postVotes = new HashSet<>();

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
  @ToString.Exclude
  private Set<Post> posts = new HashSet<>();

  @OneToMany(mappedBy = "moderator", fetch = FetchType.LAZY, orphanRemoval = true)
  @ToString.Exclude
  private Set<Post> moderatedPosts = new HashSet<>();

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
  @ToString.Exclude
  private Set<PostComment> postComments = new HashSet<>();

  /**
   * New user constructor.
   */
  public User(String name, String email) {
    this.name = name;
    this.email = email;
    regTime = LocalDateTime.now(ZoneId.ofOffset("UTC", ZoneOffset.UTC));
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
    User comparedUser = (User) that;
    return getId() != null && Objects.equals(getId(), comparedUser.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
            : getClass().hashCode();
  }
}