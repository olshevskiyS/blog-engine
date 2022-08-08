package ru.olshevskiy.blogengine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.olshevskiy.blogengine.model.dto.GetPostsDto;
import ru.olshevskiy.blogengine.service.PostServiceImpl;

/**
 * ApiPostController.
 *
 * @author Sergey Olshevskiy
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiPostController {

  private final PostServiceImpl postResource;

  @GetMapping("/post")
  private ResponseEntity<GetPostsDto> getAllActivePostsBySorting(
          @RequestParam(defaultValue = "0", required = false) int offset,
          @RequestParam(defaultValue = "10", required = false) int limit,
          @RequestParam(defaultValue = "recent", required = false) String mode) {
    return new ResponseEntity<>(postResource.getPosts(offset, limit, mode), HttpStatus.OK);
  }
}
