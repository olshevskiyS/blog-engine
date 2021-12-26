package ru.olshevskiy.blogengine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер для обычных запросов не через API.
 */
@Controller
public class DefaultController {

  @RequestMapping("/")
  public String index() {
    return "index";
  }
}
