package ru.olshevskiy.blogengine.service;

import ru.olshevskiy.blogengine.model.dto.response.CaptchaRs;

/**
 * CaptchaService.
 *
 * @author Sergey Olshevskiy
 */
public interface CaptchaService {

  CaptchaRs getCaptcha();
}
