package ru.olshevskiy.blogengine.resource;

import org.springframework.web.bind.annotation.RestController;

/**
 * DefaultResource.
 *
 * @author Sergey Olshevskiy
 */
@RestController
public class DefaultResource implements DefaultController {

  @Override
  public String index() {
    return "index";
  }
}
