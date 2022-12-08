package ru.olshevskiy.blogengine.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.olshevskiy.blogengine.model.dto.request.LoginRq;
import ru.olshevskiy.blogengine.model.dto.request.RegistrationRq;
import ru.olshevskiy.blogengine.model.dto.response.LoginAndCheckRs;
import ru.olshevskiy.blogengine.model.dto.response.LogoutRs;
import ru.olshevskiy.blogengine.model.dto.response.RegistrationRs;

/**
 * AuthService.
 *
 * @author Sergey Olshevskiy
 */
public interface AuthService {

  LoginAndCheckRs check();

  RegistrationRs register(RegistrationRq registrationRq);

  LoginAndCheckRs login(LoginRq loginRq, HttpServletRequest request, HttpServletResponse response);

  LogoutRs logout(HttpServletRequest request, HttpServletResponse response);
}
