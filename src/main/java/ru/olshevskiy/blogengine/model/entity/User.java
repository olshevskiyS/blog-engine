package ru.olshevskiy.blogengine.model.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * User.
 *
 * @author Sergey Olshevskiy
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
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

  @Column(name = "photo", columnDefinition = "text")
  private String photo;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<PostVote> postVotes = new HashSet<>();

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<Post> posts = new HashSet<>();

  @OneToMany(mappedBy = "moderator", fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<Post> moderatedPosts = new HashSet<>();

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<PostComment> postComments = new HashSet<>();

  User(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
    regTime = LocalDateTime.now();
  }
}