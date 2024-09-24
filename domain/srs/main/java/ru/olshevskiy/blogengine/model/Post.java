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
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Post.
 *
 * @author Sergey Olshevskiy
 */
@Getter
@Setter
@NoArgsConstructor
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
  private User user;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "moderator_id")
  private User moderator;

  @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<PostVote> votes = new HashSet<>();

  @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<PostComment> comments = new HashSet<>();

  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST},
          mappedBy = "posts", fetch = FetchType.LAZY)
  private Set<Tag> tags = new HashSet<>();

  /**
   * New post constructor.
   */
  public Post(String title, String text) {
    this.title = title;
    this.text = text;
    moderationStatus = ModerationStatus.NEW;
  }
}