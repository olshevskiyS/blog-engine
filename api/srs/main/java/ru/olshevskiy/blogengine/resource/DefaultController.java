package ru.olshevskiy.blogengine.resource;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * DefaultController.
 *
 * @author Sergey Olshevskiy
 */
@RequestMapping("/")
public interface DefaultController {

  String index();
}
