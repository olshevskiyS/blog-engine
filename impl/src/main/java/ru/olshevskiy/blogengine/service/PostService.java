package ru.olshevskiy.blogengine.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.olshevskiy.blogengine.dto.ErrorDescription;
import ru.olshevskiy.blogengine.dto.PostDto;
import ru.olshevskiy.blogengine.dto.request.AddPostCommentRq;
import ru.olshevskiy.blogengine.dto.request.CreatePostRq;
import ru.olshevskiy.blogengine.dto.request.DislikePostRq;
import ru.olshevskiy.blogengine.dto.request.EditPostRq;
import ru.olshevskiy.blogengine.dto.request.LikePostRq;
import ru.olshevskiy.blogengine.dto.request.ModerationPostRq;
import ru.olshevskiy.blogengine.dto.request.PostVoteRq;
import ru.olshevskiy.blogengine.dto.response.AddPostCommentRs;
import ru.olshevskiy.blogengine.dto.response.CreatePostRs;
import ru.olshevskiy.blogengine.dto.response.DislikePostRs;
import ru.olshevskiy.blogengine.dto.response.EditPostRs;
import ru.olshevskiy.blogengine.dto.response.GetPostsRs;
import ru.olshevskiy.blogengine.dto.response.LikePostRs;
import ru.olshevskiy.blogengine.dto.response.ModerationPostRs;
import ru.olshevskiy.blogengine.dto.response.ModerationPostsRs;
import ru.olshevskiy.blogengine.dto.response.MyPostsRs;
import ru.olshevskiy.blogengine.dto.response.PostByIdRs;
import ru.olshevskiy.blogengine.dto.response.PostsByDateRs;
import ru.olshevskiy.blogengine.dto.response.PostsByQueryRs;
import ru.olshevskiy.blogengine.dto.response.PostsByTagRs;
import ru.olshevskiy.blogengine.exception.ex.InvalidCommentException;
import ru.olshevskiy.blogengine.exception.ex.WrongParamInputException;
import ru.olshevskiy.blogengine.mapper.PostViewPostDtoMapper;
import ru.olshevskiy.blogengine.model.GlobalSetting;
import ru.olshevskiy.blogengine.model.ModerationStatus;
import ru.olshevskiy.blogengine.model.Post;
import ru.olshevskiy.blogengine.model.PostComment;
import ru.olshevskiy.blogengine.model.PostVote;
import ru.olshevskiy.blogengine.model.Tag;
import ru.olshevskiy.blogengine.model.User;
import ru.olshevskiy.blogengine.projection.PostView;
import ru.olshevskiy.blogengine.repository.GlobalSettingRepository;
import ru.olshevskiy.blogengine.repository.PostCommentRepository;
import ru.olshevskiy.blogengine.repository.PostRepository;
import ru.olshevskiy.blogengine.repository.PostVoteRepository;
import ru.olshevskiy.blogengine.repository.TagRepository;
import ru.olshevskiy.blogengine.repository.UserRepository;
import ru.olshevskiy.blogengine.security.SecurityUtils;

/**
 * PostService.
 *
 * @author Sergey Olshevskiy
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final PostViewPostDtoMapper postViewPostDtoMapper;
  private final TagRepository tagRepository;
  private final PostCommentRepository postCommentRepository;
  private final UserRepository userRepository;
  private final PostVoteRepository postVoteRepository;
  private final GlobalSettingRepository globalSettingRepository;

  /**
   * PostService. Getting all active posts method.
   */
  public GetPostsRs getPosts(int offset, int limit, String mode) {
    log.info("Start request getPosts with offset = {}, limit = {}, sorting mode = {}",
            offset, limit, mode);
    GetPostsRs postsRs = new GetPostsRs();
    int pageNumber = offset / limit;
    Page<PostView> page = postRepository.getActivePosts(
            PageRequest.of(pageNumber, limit, sortingMode(mode)));
    List<PostDto> postsList = page.getContent().stream()
            .map(postViewPostDtoMapper::postViewToPostDto).collect(Collectors.toList());
    postsRs.setCount(page.getTotalElements())
           .setPosts(postsList);
    log.info("Finish request getPosts");
    return postsRs;
  }

  private Sort sortingMode(String mode) {
    return switch (mode) {
      case "early" -> Sort.by(Sort.Direction.ASC, "time");
      case "popular" -> JpaSort.unsafe(Sort.Direction.DESC, "size(p.comments)")
                .and(Sort.by(Sort.Direction.DESC, "time"));
      case "best" -> JpaSort.unsafe(Sort.Direction.DESC, "likeCount")
                .and(Sort.by(Sort.Direction.DESC, "time"));
      default -> Sort.by(Sort.Direction.DESC, "time");
    };
  }

  /**
   * PostService. Getting posts by query method.
   */
  public PostsByQueryRs getPostsByQuery(int offset, int limit, String query) {
    log.info("Start request getPostsByQuery with offset = {}, limit = {}, query = {}",
            offset, limit, query);
    PostsByQueryRs postsByQueryRs = new PostsByQueryRs();
    Sort sortingMode = Sort.by(Sort.Direction.DESC, "time");
    int pageNumber = offset / limit;
    Page<PostView> page;
    if (query.isEmpty() || query.matches("\\s+")) {
      page = postRepository.getActivePosts(PageRequest.of(pageNumber, limit, sortingMode));
    } else {
      page = postRepository.getActivePostsByQuery(query,
              PageRequest.of(pageNumber, limit, sortingMode));
    }
    List<PostDto> postsList = page.getContent().stream()
            .map(postViewPostDtoMapper::postViewToPostDto).collect(Collectors.toList());
    postsByQueryRs.setCount(page.getTotalElements())
                  .setPosts(postsList);
    log.info("Finish request getPostsByQuery = {}", query);
    return postsByQueryRs;
  }

  /**
   * PostService. Getting posts by date method.
   */
  public PostsByDateRs getPostsByDate(int offset, int limit, String date) {
    log.info("Start request getPostsByDate with offset = {}, limit = {}, date = {}",
            offset, limit, date);
    PostsByDateRs postsByDateRs = new PostsByDateRs();
    int pageNumber = offset / limit;
    Page<PostView> page = postRepository.getActivePostsByDate(date,
            PageRequest.of(pageNumber, limit, Sort.by(Sort.Direction.DESC, "time")));
    List<PostDto> postsList = page.getContent().stream()
            .map(postViewPostDtoMapper::postViewToPostDto).collect(Collectors.toList());
    postsByDateRs.setCount(page.getTotalElements())
                 .setPosts(postsList);
    log.info("Finish request getPostsByDate = {}", date);
    return postsByDateRs;
  }

  /**
   * PostService. Getting posts by tag method.
   */
  public PostsByTagRs getPostsByTag(int offset, int limit, String tag) {
    log.info("Start request getPostsByTag with offset = {}, limit = {}, tag = {}",
            offset, limit, tag);
    PostsByTagRs postsByTagRs = new PostsByTagRs();
    int pageNumber = offset / limit;
    Page<PostView> page = postRepository.getActivePostsByTag(tag,
            PageRequest.of(pageNumber, limit, Sort.by(Sort.Direction.DESC, "time")));
    List<PostDto> postsList = page.getContent().stream()
            .map(postViewPostDtoMapper::postViewToPostDto).collect(Collectors.toList());
    postsByTagRs.setCount(page.getTotalElements())
                .setPosts(postsList);
    log.info("Finish request getPostsByTag = {}", tag);
    return postsByTagRs;
  }

  /**
   * PostService. Getting a post by id method.
   */
  public PostByIdRs getPostById(int id) {
    log.info("Start request getPostById, id = {}", id);
    Optional<PostView> optionalPostView = postRepository.getPostById(id);
    if (optionalPostView.isEmpty()) {
      log.info("Post not found. Finish request getPostById with exception");
      throw new WrongParamInputException(ErrorDescription.POST_NOT_FOUND);
    }
    Post updatedPost = updateCurrentPostViewCount(optionalPostView.get().getPost());
    PostByIdRs postByIdRs = postViewPostDtoMapper.postViewToPostById(
            optionalPostView.get(), updatedPost.getViewCount());
    log.info("Finish request getPostById = {}", id);
    return postByIdRs;
  }

  private Post updateCurrentPostViewCount(Post currentPost) {
    if (SecurityUtils.userIsNotAuthorized()) {
      currentPost.setViewCount(currentPost.getViewCount() + 1);
      return postRepository.save(currentPost);
    }
    User currentUser = SecurityUtils.getCurrentUser();
    if (currentPost.getUserId() != currentUser.getId() && currentUser.getIsModerator() == 0) {
      currentPost.setViewCount(currentPost.getViewCount() + 1);
      return postRepository.save(currentPost);
    } else {
      return currentPost;
    }
  }

  /**
   * PostService. Getting all posts of the current user method.
   */
  public MyPostsRs getMyPosts(int offset, int limit, String status) {
    log.info("Start request getMyPosts with offset = {}, limit = {}, status = {}",
            offset, limit, status);
    MyPostsRs myPostsRs = new MyPostsRs();
    int pageNumber = offset / limit;
    PageRequest pageRequest = PageRequest.of(
            pageNumber, limit, Sort.by(Sort.Direction.DESC, "time"));
    Page<PostView> page = getMyPostsDependingOnModerationStatus(status,
            SecurityUtils.getCurrentUser().getId(), pageRequest);
    List<PostDto> postsList = page.getContent().stream()
            .map(postViewPostDtoMapper::postViewToPostDto).collect(Collectors.toList());
    myPostsRs.setCount(page.getTotalElements())
             .setPosts(postsList);
    log.info("Finish request getMyPosts");
    return myPostsRs;
  }

  private Page<PostView> getMyPostsDependingOnModerationStatus(String status, int userId,
                                                               PageRequest pageRequest) {
    return switch (status) {
      case "inactive" -> postRepository.getMyInactivePosts(userId, pageRequest);
      case "pending" -> postRepository.getMyActivePostsDependingOnStatus(
                userId, ModerationStatus.NEW, pageRequest);
      case "declined" -> postRepository.getMyActivePostsDependingOnStatus(
                userId, ModerationStatus.DECLINED, pageRequest);
      default -> postRepository.getMyActivePostsDependingOnStatus(
                userId, ModerationStatus.ACCEPTED, pageRequest);
    };
  }

  /**
   * PostService. Getting moderation posts of the current moderator method.
   */
  public ModerationPostsRs getModerationPosts(int offset, int limit, String status) {
    log.info("Start request getModerationPosts with offset = {}, limit = {}, status = {}",
            offset, limit, status);
    ModerationPostsRs moderationPostsRs = new ModerationPostsRs();
    int pageNumber = offset / limit;
    PageRequest pageRequest = PageRequest.of(
            pageNumber, limit, Sort.by(Sort.Direction.DESC, "time"));
    Page<PostView> page = getModerationPostsDependingOnModerationStatus(status,
            SecurityUtils.getCurrentUser().getId(), pageRequest);
    List<PostDto> postsList = page.getContent().stream()
            .map(postViewPostDtoMapper::postViewToPostDto).collect(Collectors.toList());
    moderationPostsRs.setCount(page.getTotalElements())
                     .setPosts(postsList);
    log.info("Finish request getModerationPosts");
    return moderationPostsRs;
  }

  private Page<PostView> getModerationPostsDependingOnModerationStatus(
          String status, int moderatorId, PageRequest pageRequest) {
    return switch (status) {
      case "new" -> postRepository.getModerationPostsDependingOnStatus(
                moderatorId, ModerationStatus.NEW, pageRequest);
      case "declined" -> postRepository.getModerationPostsDependingOnStatus(
                moderatorId, ModerationStatus.DECLINED, pageRequest);
      default -> postRepository.getModerationPostsDependingOnStatus(
                moderatorId, ModerationStatus.ACCEPTED, pageRequest);
    };
  }

  /**
   * PostService. Create a new post method.
   */
  public CreatePostRs createPost(CreatePostRq createPostRq) {
    log.info("Start request createPost");
    User currentUser = SecurityUtils.getCurrentUser();
    Post newPost = new Post(createPostRq.getTitle(), createPostRq.getText())
            .setIsActive(createPostRq.getActive())
            .setUserId(currentUser.getId())
            .setUser(currentUser);
    if (newPost.getIsActive() == 1) {
      if (currentUser.getIsModerator() == 1) {
        newPost.setModerationStatus(ModerationStatus.ACCEPTED);
      }
      checkingPostPremoderationModeIsActive(currentUser, newPost);
      assignModeratorForPost(newPost, currentUser);
    }
    updatePostTimestamp(createPostRq.getTimestamp(), newPost);
    Post savedPost = postRepository.save(newPost);
    if (!createPostRq.getTags().isEmpty()) {
      addTagsForPost(createPostRq.getTags(), savedPost);
    }
    log.info("Finish request createPost id = {} by a user id = {}",
            savedPost.getId(), currentUser.getId());
    return new CreatePostRs().setResult(true);
  }

  private void assignModeratorForPost(Post currentPost, User currentUser) {
    User moderatorForPost;
    if (currentUser.getIsModerator() == 1) {
      moderatorForPost = currentUser;
    } else {
      List<User> allModerators = userRepository.findAllByIsModeratorEquals((byte) 1);
      int selectedId = (int) (Math.random() * allModerators.size());
      moderatorForPost = allModerators.get(selectedId);
    }
    currentPost.setModeratorId(moderatorForPost.getId())
               .setModerator(moderatorForPost);
  }

  private void updatePostTimestamp(long givenTimestamp, Post updatedPost) {
    Instant now = Instant.now();
    if (givenTimestamp <= now.getEpochSecond()) {
      updatedPost.setTime(LocalDateTime.ofInstant(now, ZoneId.ofOffset("UTC", ZoneOffset.UTC)));
    } else {
      updatedPost.setTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(givenTimestamp),
              ZoneId.ofOffset("UTC", ZoneOffset.UTC)));
    }
  }

  private void checkingPostPremoderationModeIsActive(User currentUser, Post currentPost) {
    GlobalSetting globalSetting = globalSettingRepository.findByCode("POST_PREMODERATION");
    if (currentUser.getIsModerator() == 0 && globalSetting.getValue().equals("NO")) {
      currentPost.setModerationStatus(ModerationStatus.ACCEPTED);
    }
  }

  private void addTagsForPost(List<String> givenTagsNames, Post updatedPost) {
    for (String tagName : givenTagsNames) {
      Optional<Tag> optionalTag = tagRepository.findByName(tagName);
      if (optionalTag.isPresent()) {
        Tag tag = optionalTag.get();
        updatedPost.getTags().add(tag);
        tag.getPosts().add(updatedPost);
      } else {
        Tag newTag = new Tag(tagName);
        tagRepository.save(newTag);
        updatedPost.getTags().add(newTag);
        newTag.getPosts().add(updatedPost);
      }
    }
  }

  /**
   * PostService. Edit post by id method.
   */
  public EditPostRs editPost(int id, EditPostRq editPostRq) {
    log.info("Start request editPost id {}", id);
    Post currentPost = postRepository.getReferenceById(id);
    User currentUser = SecurityUtils.getCurrentUser();
    if (currentPost.getIsActive() == 0 && editPostRq.getActive() == 1) {
      assignModeratorForPost(currentPost, currentUser);
    }
    currentPost.setIsActive(editPostRq.getActive())
               .setTitle(editPostRq.getTitle())
               .setText(editPostRq.getText());
    updateModerationStatusForPost(currentPost, currentUser);
    updatePostTimestamp(editPostRq.getTimestamp(), currentPost);
    updateTagsForPost(editPostRq.getTags(), currentPost);
    log.info("Finish request editPost id {}", id);
    return new EditPostRs().setResult(true);
  }

  private void updateModerationStatusForPost(Post currentPost, User currentUser) {
    if (currentUser.getId() == currentPost.getUserId()) {
      currentPost.setModerationStatus(ModerationStatus.NEW);
    }
    if (currentUser.getIsModerator() == 1) {
      currentPost.setModerationStatus(ModerationStatus.ACCEPTED);
    }
  }

  private void updateTagsForPost(List<String> listOfGivenTagsNames, Post currentPost) {
    if (!listOfGivenTagsNames.isEmpty() && !currentPost.getTags().isEmpty()) {
      List<String> tagsToAdd = new ArrayList<>();
      Set<Tag> tagsToDelete = new HashSet<>();
      for (String givenTagName : listOfGivenTagsNames) {
        for (Tag postTag : currentPost.getTags()) {
          if (!givenTagName.equals(postTag.getName())) {
            tagsToAdd.add(givenTagName);
          }
          if (!listOfGivenTagsNames.contains(postTag.getName())) {
            tagsToDelete.add(postTag);
          }
        }
      }
      addTagsForPost(tagsToAdd, currentPost);
      for (Tag tag : tagsToDelete) {
        currentPost.getTags().remove(tag);
        tag.getPosts().remove(currentPost);
      }
    } else if (!listOfGivenTagsNames.isEmpty()) {
      addTagsForPost(listOfGivenTagsNames, currentPost);
    }
  }

  /**
   * PostService. Add comment for post or parent comment method.
   */
  public AddPostCommentRs addPostComment(AddPostCommentRq addPostCommentRq) {
    log.info("Start request addPostComment for post with id {}", addPostCommentRq.getPostId());
    checkLengthCommentText(addPostCommentRq.getText());
    User currentUser = SecurityUtils.getCurrentUser();
    Post currentPost = getPostForComment(addPostCommentRq.getPostId());
    PostComment newPostComment = new PostComment(
            currentUser.getId(), currentPost.getId(), addPostCommentRq.getText());
    newPostComment.setUser(currentUser);
    newPostComment.setPost(currentPost);
    PostComment currentParentPostComment = checkAndGetCurrentParentComment(
            addPostCommentRq.getParentId());
    if (currentParentPostComment != null) {
      newPostComment.setParentId(currentParentPostComment.getId());
      newPostComment.setParentComment(currentParentPostComment);
    }
    PostComment savedPostComment = postCommentRepository.save(newPostComment);
    log.info("Finish request addPostComment, new comment id {}", savedPostComment.getId());
    return new AddPostCommentRs().setId(savedPostComment.getId());
  }

  private void checkLengthCommentText(String textComment) {
    if (textComment.length() < 3) {
      log.info("Comment's text is too small. Finish request addPostComment with exception");
      throw new InvalidCommentException();
    }
  }

  private Post getPostForComment(int requestedPostId) {
    Optional<Post> optionalPost = postRepository.findById(requestedPostId);
    if (optionalPost.isEmpty()) {
      log.info("Post not found. Finish request addPostComment with exception");
      throw new WrongParamInputException(ErrorDescription.POST_NOT_FOUND);
    }
    return optionalPost.get();
  }

  private PostComment checkAndGetCurrentParentComment(Integer requestedParentCommentId) {
    if (requestedParentCommentId == null) {
      return null;
    }
    Optional<PostComment> optionalPostComment = postCommentRepository
            .findById(requestedParentCommentId);
    if (optionalPostComment.isEmpty()) {
      log.info("Post's comment not found. Finish request addPostComment with exception");
      throw new WrongParamInputException(ErrorDescription.COMMENT_NOT_FOUND);
    }
    return optionalPostComment.get();
  }

  /**
   * PostService. Moderate the post method.
   */
  public ModerationPostRs moderatePost(ModerationPostRq moderationPostRq) {
    log.info("Start request moderatePost with id {}", moderationPostRq.getPostId());
    Optional<Post> optionalPost = postRepository.findById(moderationPostRq.getPostId());
    if (postDoNotExistsByRequest("moderatePost", optionalPost)) {
      return new ModerationPostRs().setResult(false);
    }
    Post currentPost = optionalPost.get();
    if (moderationPostRq.getDecision().equals("accept")) {
      currentPost.setModerationStatus(ModerationStatus.ACCEPTED);
    } else {
      currentPost.setModerationStatus(ModerationStatus.DECLINED);
    }
    postRepository.save(currentPost);
    log.info("Finish request moderatePost. Status: {}", moderationPostRq.getDecision());
    return new ModerationPostRs().setResult(true);
  }

  private boolean postDoNotExistsByRequest(String request, Optional<Post> postOptional) {
    if (postOptional.isEmpty()) {
      log.info("Post not found. Request {} failed", request);
      return true;
    }
    return false;
  }

  /**
   * PostService. Like the post method.
   */
  public LikePostRs like(LikePostRq likePostRq) {
    log.info("Start request like for post id {}", likePostRq.getPostId());
    User currentUser = SecurityUtils.getCurrentUser();
    Optional<PostVote> postVoteOptional = postVoteRepository
            .findByPostIdAndUserId(likePostRq.getPostId(), currentUser.getId());
    if (postVoteOptional.isEmpty()) {
      Optional<Post> optionalPost = postRepository.findById(likePostRq.getPostId());
      if (postDoNotExistsByRequest("like", optionalPost)) {
        return (LikePostRs) new LikePostRs().setResult(false);
      }
      createAndSaveNewVote(likePostRq, currentUser, optionalPost.get());
    } else {
      PostVote currentPostVote = postVoteOptional.get();
      if (voteIsRepeated(likePostRq, currentPostVote)) {
        return (LikePostRs) new LikePostRs().setResult(false);
      }
      replaceAndSaveExistingVote(likePostRq, currentPostVote);
    }
    log.info("Finish request like");
    return (LikePostRs) new LikePostRs().setResult(true);
  }

  /**
   * PostService. Dislike the post method.
   */
  public DislikePostRs dislike(DislikePostRq dislikePostRq) {
    log.info("Start request dislike for post id {}", dislikePostRq.getPostId());
    User currentUser = SecurityUtils.getCurrentUser();
    Optional<PostVote> postVoteOptional = postVoteRepository
            .findByPostIdAndUserId(dislikePostRq.getPostId(), currentUser.getId());
    if (postVoteOptional.isEmpty()) {
      Optional<Post> optionalPost = postRepository.findById(dislikePostRq.getPostId());
      if (postDoNotExistsByRequest("dislike", optionalPost)) {
        return (DislikePostRs) new DislikePostRs().setResult(false);
      }
      createAndSaveNewVote(dislikePostRq, currentUser, optionalPost.get());
    } else {
      PostVote currentPostVote = postVoteOptional.get();
      if (voteIsRepeated(dislikePostRq, currentPostVote)) {
        return (DislikePostRs) new DislikePostRs().setResult(false);
      }
      replaceAndSaveExistingVote(dislikePostRq, currentPostVote);
    }
    log.info("Finish request dislike");
    return (DislikePostRs) new DislikePostRs().setResult(true);
  }

  private void createAndSaveNewVote(PostVoteRq request, User currentUser, Post currentPost) {
    byte value = determineVoteValueDependingOnRequest(request);
    PostVote newPostVote = new PostVote(currentUser.getId(), currentPost.getId(), value);
    newPostVote.setUser(currentUser)
               .setPost(currentPost);
    postVoteRepository.save(newPostVote);
  }

  private byte determineVoteValueDependingOnRequest(PostVoteRq request) {
    byte value = -1;
    if (request instanceof LikePostRq) {
      value = 1;
    }
    return value;
  }

  private boolean voteIsRepeated(PostVoteRq request, PostVote currentPostVote) {
    byte value = determineVoteValueDependingOnRequest(request);
    StringBuilder requestName = new StringBuilder("dislike");
    if (value == 1) {
      requestName.replace(0, 7, "like");
    }
    if (currentPostVote.getValue() == value) {
      log.info("Repeat vote. Request {} failed", requestName);
      return true;
    }
    return false;
  }

  private void replaceAndSaveExistingVote(PostVoteRq request, PostVote currentPostVote) {
    byte value = determineVoteValueDependingOnRequest(request);
    currentPostVote.setValue(value)
                   .setTime(LocalDateTime.now(ZoneId.ofOffset("UTC", ZoneOffset.UTC)));
    postVoteRepository.save(currentPostVote);
  }
}