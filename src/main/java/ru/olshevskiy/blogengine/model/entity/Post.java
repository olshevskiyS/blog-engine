package ru.olshevskiy.blogengine.model.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс сущности постов.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
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
  private int moderatorId;

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

  Post(int userId, String title, String text) {
    this.userId = userId;
    this.title = title;
    this.text = text;
    moderationStatus = ModerationStatus.NEW;
  }

  private enum ModerationStatus {
    NEW, ACCEPTED, DECLINED
  }
}