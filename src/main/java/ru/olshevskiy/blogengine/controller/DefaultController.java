package ru.olshevskiy.blogengine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * DefaultController.
 *
 * @author Sergey Olshevskiy
 */
@Controller
public class DefaultController {

  @RequestMapping("/")
  public String index() {
    return "index";
  }
}
