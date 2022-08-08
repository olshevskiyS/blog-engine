package ru.olshevskiy.blogengine.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.olshevskiy.blogengine.model.dto.CheckAuthorizationDto;

/**
 * UserServiceImpl.
 *
 * @author Sergey Olshevskiy
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

  @Override
  public CheckAuthorizationDto getCheckAuthorization() {
    log.info("Start request getCheckAuthorization");
    CheckAuthorizationDto authorization = new CheckAuthorizationDto();
    authorization.setResult(false);
    log.info("Finish request getCheckAuthorization");
    return authorization;
  }
}
