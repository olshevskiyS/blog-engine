package ru.olshevskiy.blogengine.resource;

import org.springframework.stereotype.Controller;

/**
 * DefaultResource.
 *
 * @author Sergey Olshevskiy
 */
@Controller
public class DefaultResource implements DefaultController {

  @Override
  public String index() {
    return "index";
  }

  @Override
  public String redirectToIndex(String path) {
    return "forward:/";
  }
}
