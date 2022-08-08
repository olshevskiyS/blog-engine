package ru.olshevskiy.blogengine.service;

import ru.olshevskiy.blogengine.model.dto.CheckAuthorizationDto;

/**
 * UserService.
 *
 * @author Sergey Olshevskiy
 */
public interface UserService {

  CheckAuthorizationDto getCheckAuthorization();
}
