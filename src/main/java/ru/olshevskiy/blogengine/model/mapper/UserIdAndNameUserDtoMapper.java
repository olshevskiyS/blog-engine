package ru.olshevskiy.blogengine.model.mapper;

import org.mapstruct.Mapper;
import ru.olshevskiy.blogengine.model.dto.IdAndNameUserDto;
import ru.olshevskiy.blogengine.model.entity.User;

/**
 * Интерфейс маппинга сущности User в IdAndNameUserDto.
 */
@Mapper(componentModel = "spring")
public interface UserIdAndNameUserDtoMapper {

  IdAndNameUserDto userToIdAndNameUserDto(User user);
}
