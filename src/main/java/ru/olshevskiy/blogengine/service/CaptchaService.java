package ru.olshevskiy.blogengine.service;

import ru.olshevskiy.blogengine.model.dto.CaptchaDto;

/**
 * CaptchaService.
 *
 * @author Sergey Olshevskiy
 */
public interface CaptchaService {

  CaptchaDto getCaptcha();
}
