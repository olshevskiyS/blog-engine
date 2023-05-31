package ru.olshevskiy.blogengine.resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * DefaultController.
 *
 * @author Sergey Olshevskiy
 */
public interface DefaultController {

  @RequestMapping("/")
  String index();

  @RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET}, value = "/**/{path:[^.]*}")
  String redirectToIndex(@PathVariable String path);
}
