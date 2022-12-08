package ru.olshevskiy.blogengine.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.olshevskiy.blogengine.model.dto.UserDto;
import ru.olshevskiy.blogengine.model.entity.User;

/**
 * UserUserDtoMapper.
 *
 * @author Sergey Olshevskiy
 */
@Mapper(componentModel = "spring")
public interface UserUserDtoMapper {

  @Mapping(target = "moderation",
           expression = "java(UserDtoMapperSupport.checkModerationOption(user))")
  @Mapping(target = "moderationCount", source = "countModerationPosts")
  @Mapping(target = "settings",
           expression = "java(UserDtoMapperSupport.checkModerationOption(user))")
  UserDto userToUserDto(User user, int countModerationPosts);
}
