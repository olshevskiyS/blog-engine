package ru.olshevskiy.blogengine.mapper;

import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.olshevskiy.blogengine.dto.IdAndNameUserDto;
import ru.olshevskiy.blogengine.dto.IdNamePhotoUserDto;
import ru.olshevskiy.blogengine.dto.PostCommentDto;
import ru.olshevskiy.blogengine.dto.PostDto;
import ru.olshevskiy.blogengine.dto.response.PostByIdRs;
import ru.olshevskiy.blogengine.model.PostComment;
import ru.olshevskiy.blogengine.model.Tag;
import ru.olshevskiy.blogengine.model.User;
import ru.olshevskiy.blogengine.projection.PostView;

/**
 * PostViewPostDtoMapper.
 *
 * @author Sergey Olshevskiy
 */
@Mapper(componentModel = "spring")
public interface PostViewPostDtoMapper {

  @Mappings({
      @Mapping(target = "id", source = "post.id"),
      @Mapping(target = "timestamp", expression =
              "java(PostViewMapperSupport.convertTimeToSeconds(postView.getPost().getTime()))"),
      @Mapping(target = "user", source = "post.user"),
      @Mapping(target = "title", source = "post.title"),
      @Mapping(target = "announce",
              expression = "java(PostViewMapperSupport.getAnnounceFromText(postView))"),
      @Mapping(target = "viewCount", source = "post.viewCount"),
      @Mapping(target = "commentCount",
              expression = "java(postView.getPost().getComments().size())")
  })
  PostDto postViewToPostDto(PostView postView);

  IdAndNameUserDto userToIdAndNameUserDto(User user);

  @Mappings({
      @Mapping(target = "id", source = "postView.post.id"),
      @Mapping(target = "timestamp", expression =
              "java(PostViewMapperSupport.convertTimeToSeconds(postView.getPost().getTime()))"),
      @Mapping(target = "active", expression =
              "java(PostViewMapperSupport.checkPostIsActive(postView.getPost()))"),
      @Mapping(target = "user", source = "postView.post.user"),
      @Mapping(target = "title", source = "postView.post.title"),
      @Mapping(target = "text", source = "postView.post.text"),
      @Mapping(target = "viewCount", source = "actualViewCount"),
      @Mapping(target = "comments", source = "postView.post.comments"),
      @Mapping(target = "tags", source = "postView.post.tags")
  })
  PostByIdRs postViewToPostById(PostView postView, int actualViewCount);

  Set<PostCommentDto> postCommentToPostCommentDtoList(Set<PostComment> comments);

  @Mapping(target = "timestamp",
          expression = "java(PostViewMapperSupport.convertTimeToSeconds(postComment.getTime()))")
  PostCommentDto postCommentToPostCommentDto(PostComment postComment);

  IdNamePhotoUserDto userToIdNamePhotoUserDto(User user);

  Set<String> tagToTagNameList(Set<Tag> tags);

  default String tagToTagName(Tag tag) {
    return tag.getName();
  }
}
