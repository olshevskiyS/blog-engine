package ru.olshevskiy.blogengine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.olshevskiy.blogengine.model.dto.response.GetPostsRs;
import ru.olshevskiy.blogengine.model.dto.response.MyPostsRs;
import ru.olshevskiy.blogengine.model.dto.response.PostByIdRs;
import ru.olshevskiy.blogengine.model.dto.response.PostsByDateRs;
import ru.olshevskiy.blogengine.model.dto.response.PostsByQueryRs;
import ru.olshevskiy.blogengine.model.dto.response.PostsByTagRs;
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
  public ResponseEntity<GetPostsRs> getPosts(
          @RequestParam(defaultValue = "0") int offset,
          @RequestParam(defaultValue = "10") int limit,
          @RequestParam(defaultValue = "recent") String mode) {
    return new ResponseEntity<>(postResource.getPosts(offset, limit, mode), HttpStatus.OK);
  }

  @GetMapping("/search")
  public ResponseEntity<PostsByQueryRs> getPostsByQuery(
          @RequestParam(defaultValue = "0") int offset,
          @RequestParam(defaultValue = "10") int limit,
          @RequestParam String query) {
    return new ResponseEntity<>(postResource.getPostsByQuery(offset, limit, query), HttpStatus.OK);
  }

  @GetMapping("/byDate")
  public ResponseEntity<PostsByDateRs> getPostsByDate(
          @RequestParam(defaultValue = "0") int offset,
          @RequestParam(defaultValue = "10") int limit,
          @RequestParam String date) {
    return new ResponseEntity<>(postResource.getPostsByDate(offset, limit, date), HttpStatus.OK);
  }

  @GetMapping("/byTag")
  public ResponseEntity<PostsByTagRs> getPostsByTag(
          @RequestParam(defaultValue = "0") int offset,
          @RequestParam(defaultValue = "10") int limit,
          @RequestParam String tag) {
    return new ResponseEntity<>(postResource.getPostsByTag(offset, limit, tag), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostByIdRs> getPostById(@PathVariable("id") int id) {
    return new ResponseEntity<>(postResource.getPostById(id), HttpStatus.OK);
  }

  @GetMapping("/my")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<MyPostsRs> getMyPosts(
          @RequestParam(defaultValue = "0") int offset,
          @RequestParam(defaultValue = "10") int limit,
          @RequestParam String status) {
    return new ResponseEntity<>(postResource.getMyPosts(offset, limit, status), HttpStatus.OK);
  }
}