package ru.olshevskiy.blogengine.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.olshevskiy.blogengine.dto.response.GetPostsRs;
import ru.olshevskiy.blogengine.dto.response.MyPostsRs;
import ru.olshevskiy.blogengine.dto.response.PostByIdRs;
import ru.olshevskiy.blogengine.dto.response.PostsByDateRs;
import ru.olshevskiy.blogengine.dto.response.PostsByQueryRs;
import ru.olshevskiy.blogengine.dto.response.PostsByTagRs;

/**
 * ApiPostController.
 *
 * @author Sergey Olshevskiy
 */
@RequestMapping("/api/post")
@Tag(name = "Post Service", description = "Работа с постами")
public interface ApiPostController {

  @GetMapping("")
  @Operation(summary = "Список постов для главной страницы")
  @ApiResponse(responseCode = "200",
               description = "Успешный запрос",
               content = @Content(mediaType = "application/json",
               schema = @Schema(implementation = GetPostsRs.class)))
  ResponseEntity<GetPostsRs> getPosts(
          @Parameter(description = "Сдвиг от 0 для постраничного вывода")
            @RequestParam(defaultValue = "0") int offset,
          @Parameter(description = "Количество постов, которое надо вывести")
            @RequestParam(defaultValue = "10") int limit,
          @Parameter(description = "Режим вывода (сортировка)")
            @RequestParam(defaultValue = "recent") String mode);

  @GetMapping("/search")
  @Operation(summary = "Поиск постов")
  @ApiResponse(responseCode = "200",
               description = "Успешный запрос",
               content = @Content(mediaType = "application/json",
               schema = @Schema(implementation = PostsByQueryRs.class)))
  ResponseEntity<PostsByQueryRs> getPostsByQuery(
          @Parameter(description = "Сдвиг от 0 для постраничного вывода")
            @RequestParam(defaultValue = "0") int offset,
          @Parameter(description = "Количество постов, которое надо вывести")
            @RequestParam(defaultValue = "10") int limit,
          @Parameter(description = "Поисковый запрос")
            @RequestParam String query);

  @GetMapping("/byDate")
  @Operation(summary = "Список постов за указанную дату")
  @ApiResponse(responseCode = "200",
               description = "Успешный запрос",
               content = @Content(mediaType = "application/json",
               schema = @Schema(implementation = PostsByDateRs.class)))
  ResponseEntity<PostsByDateRs> getPostsByDate(
          @Parameter(description = "Сдвиг от 0 для постраничного вывода")
            @RequestParam(defaultValue = "0") int offset,
          @Parameter(description = "Количество постов, которое надо вывести")
            @RequestParam(defaultValue = "10") int limit,
          @Parameter(description = "Дата, по которой нужно вывести посты")
            @RequestParam String date);

  @GetMapping("/byTag")
  @Operation(summary = "Список постов по тэгу")
  @ApiResponse(responseCode = "200",
               description = "Успешный запрос",
               content = @Content(mediaType = "application/json",
               schema = @Schema(implementation = PostsByTagRs.class)))
  ResponseEntity<PostsByTagRs> getPostsByTag(
          @Parameter(description = "Сдвиг от 0 для постраничного вывода")
            @RequestParam(defaultValue = "0") int offset,
          @Parameter(description = "Количество постов, которое надо вывести")
            @RequestParam(defaultValue = "10") int limit,
          @Parameter(description = "Тэг, по которому нужно вывести посты")
            @RequestParam String tag);

  @GetMapping("/{id}")
  @Operation(summary = "Получение поста по идентификатору")
  @ApiResponses({
      @ApiResponse(responseCode = "200",
                   description = "Успешный запрос",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = PostByIdRs.class))),
      @ApiResponse(responseCode = "404",
                   description = "Пост не найден",
                   content = @Content(mediaType = "application/json"))
  })
  ResponseEntity<PostByIdRs> getPostById(@Parameter(description = "Идентификатор поста")
                                         @PathVariable("id") int id);

  @GetMapping("/my")
  @PreAuthorize("hasAuthority('user:write')")
  @Operation(summary = "Список моих постов")
  @ApiResponses({
      @ApiResponse(responseCode = "200",
                  description = "Успешный запрос",
                  content = @Content(mediaType = "application/json",
                          schema = @Schema(implementation = MyPostsRs.class))),
      @ApiResponse(responseCode = "401",
                  description = "Пользователь не аутентифицирован",
                  content = @Content(mediaType = "application/json"))
  })
  ResponseEntity<MyPostsRs> getMyPosts(
          @Parameter(description = "Сдвиг от 0 для постраничного вывода")
            @RequestParam(defaultValue = "0") int offset,
          @Parameter(description = "Количество постов, которое надо вывести")
            @RequestParam(defaultValue = "10") int limit,
          @Parameter(description = "Статус модерации")
            @RequestParam String status);
}