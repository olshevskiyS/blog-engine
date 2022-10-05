package ru.olshevskiy.blogengine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.olshevskiy.blogengine.model.dto.GetPostsDto;
import ru.olshevskiy.blogengine.model.dto.PostsByDateDto;
import ru.olshevskiy.blogengine.model.dto.PostsByQueryDto;
import ru.olshevskiy.blogengine.model.dto.PostsByTagDto;
import ru.olshevskiy.blogengine.service.PostServiceImpl;

/**
 * ApiPostController.
 *
 * @author Sergey Olshevskiy
 */
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class ApiPostController {

  private final PostServiceImpl postResource;

  @GetMapping("")
  public ResponseEntity<GetPostsDto> getPosts(
          @RequestParam(defaultValue = "0") int offset,
          @RequestParam(defaultValue = "10") int limit,
          @RequestParam(defaultValue = "recent") String mode) {
    return new ResponseEntity<>(postResource.getPosts(offset, limit, mode), HttpStatus.OK);
  }

  @GetMapping("/search")
  public ResponseEntity<PostsByQueryDto> getPostsByQuery(
          @RequestParam(defaultValue = "0") int offset,
          @RequestParam(defaultValue = "10") int limit,
          @RequestParam String query) {
    return new ResponseEntity<>(postResource.getPostsByQuery(offset, limit, query), HttpStatus.OK);
  }

  @GetMapping("/byDate")
  public ResponseEntity<PostsByDateDto> getPostsByDate(
          @RequestParam(defaultValue = "0") int offset,
          @RequestParam(defaultValue = "10") int limit,
          @RequestParam String date) {
    return new ResponseEntity<>(postResource.getPostsByDate(offset, limit, date), HttpStatus.OK);
  }

  @GetMapping("/byTag")
  public ResponseEntity<PostsByTagDto> getPostsByTag(
          @RequestParam(defaultValue = "0") int offset,
          @RequestParam(defaultValue = "10") int limit,
          @RequestParam String tag) {
    return new ResponseEntity<>(postResource.getPostsByTag(offset, limit, tag), HttpStatus.OK);
  }
}