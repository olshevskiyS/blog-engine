package ru.olshevskiy.blogengine.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.olshevskiy.blogengine.model.dto.IdAndNameUserDto;
import ru.olshevskiy.blogengine.model.dto.PostDto;
import ru.olshevskiy.blogengine.model.entity.User;
import ru.olshevskiy.blogengine.model.projection.PostView;

/**
 * Интерфейс маппинга сущности Post через PostView в PostDto.
 */
@Mapper(componentModel = "spring")
public interface PostViewPostDtoMapper {

  @Mappings({
      @Mapping(target = "id", source = "post.id"),
      @Mapping(target = "timestamp",
              expression = "java(PostViewPostDtoMapperSupport.convertTimeToSeconds(postView))"),
      @Mapping(target = "usersIdAndName", source = "post.user"),
      @Mapping(target = "title", source = "post.title"),
      @Mapping(target = "announce",
              expression = "java(PostViewPostDtoMapperSupport.getAnnounceFromText(postView))"),
      @Mapping(target = "viewCount", source = "post.viewCount"),
      @Mapping(target = "commentCount",
              expression = "java(postView.getPost().getComments().size())")
  })
  PostDto postViewToPostDto(PostView postView);

  IdAndNameUserDto userToIdAndNameUserDto(User user);
}
