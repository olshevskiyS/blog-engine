package ru.olshevskiy.blogengine.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.olshevskiy.blogengine.dto.Error;
import ru.olshevskiy.blogengine.dto.InvalidInput;
import ru.olshevskiy.blogengine.dto.request.AddPostCommentRq;
import ru.olshevskiy.blogengine.dto.request.CreatePostRq;
import ru.olshevskiy.blogengine.dto.request.EditPostRq;
import ru.olshevskiy.blogengine.dto.request.ModerationPostRq;
import ru.olshevskiy.blogengine.dto.response.AddPostCommentRs;
import ru.olshevskiy.blogengine.dto.response.CreatePostRs;
import ru.olshevskiy.blogengine.dto.response.EditPostRs;
import ru.olshevskiy.blogengine.dto.response.GetPostsRs;
import ru.olshevskiy.blogengine.dto.response.ModerationPostRs;
import ru.olshevskiy.blogengine.dto.response.ModerationPostsRs;
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
@RequestMapping("/api")
@Tag(name = "Post Service", description = "Работа с постами")
public interface ApiPostController {

  @GetMapping("/post")
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

  @GetMapping("/post/search")
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

  @GetMapping("/post/byDate")
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

  @GetMapping("/post/byTag")
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

  @GetMapping("/post/{id}")
  @Operation(summary = "Получение поста по идентификатору")
  @ApiResponses({
      @ApiResponse(responseCode = "200",
                   description = "Успешный запрос",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = PostByIdRs.class))),
      @ApiResponse(responseCode = "400",
                   description = "Пост не найден",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = InvalidInput.class)))
  })
  ResponseEntity<PostByIdRs> getPostById(@Parameter(description = "Идентификатор поста")
                                         @PathVariable("id") int id);

  @GetMapping("/post/my")
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

  @GetMapping("/post/moderation")
  @PreAuthorize("hasAuthority('user:moderate')")
  @Operation(summary = "Список постов на модерацию")
  @ApiResponses({
      @ApiResponse(responseCode = "200",
                   description = "Успешный запрос",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = ModerationPostsRs.class))),
      @ApiResponse(responseCode = "401",
                   description = "Пользователь не аутентифицирован",
                   content = @Content(mediaType = "application/json"))
  })
  ResponseEntity<ModerationPostsRs> getModerationPosts(
          @Parameter(description = "Сдвиг от 0 для постраничного вывода")
            @RequestParam(defaultValue = "0") int offset,
          @Parameter(description = "Количество постов, которое надо вывести")
            @RequestParam(defaultValue = "10") int limit,
          @Parameter(description = "Статус модерации")
            @RequestParam String status);

  @PostMapping("/post")
  @PreAuthorize("hasAuthority('user:write')")
  @Operation(summary = "Добавление поста")
  @ApiResponses({
      @ApiResponse(responseCode = "201",
                   description = "Успешное добавление поста",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = CreatePostRs.class))),
      @ApiResponse(responseCode = "400",
                   description = "Введены неверные данные",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = Error.class))),
      @ApiResponse(responseCode = "401",
                   description = "Пользователь не аутентифицирован",
                   content = @Content(mediaType = "application/json"))
  })
  ResponseEntity<CreatePostRs> createPost(@Valid @RequestBody CreatePostRq createPostRq);

  @PutMapping("/post/{id}")
  @PreAuthorize("hasAuthority('user:write')")
  @Operation(summary = "Редактирование поста")
  @ApiResponses({
      @ApiResponse(responseCode = "200",
                  description = "Успешное редактирование поста",
                  content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = EditPostRs.class))),
      @ApiResponse(responseCode = "400",
                  description = "Введены неверные данные",
                  content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = Error.class))),
      @ApiResponse(responseCode = "401",
                  description = "Пользователь не аутентифицирован",
                  content = @Content(mediaType = "application/json"))
  })
  ResponseEntity<EditPostRs> editPost(@Parameter(description = "Идентификатор поста")
                                      @PathVariable("id") int id,
                                      @Valid @RequestBody EditPostRq editPostRq);

  @PostMapping("/comment")
  @PreAuthorize("hasAuthority('user:write')")
  @Operation(summary = "Отправка комментария к посту")
  @ApiResponses({
      @ApiResponse(responseCode = "200",
                   description = "Успешное добавление комментария",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = AddPostCommentRs.class))),
      @ApiResponse(responseCode = "400",
                   description = "Введены неверные данные",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(oneOf = {Error.class, InvalidInput.class}),
                   examples = {@ExampleObject(name = "Текст комментария не установлен"
                                              + " или слишком короткий",
                                              value = addPostCommentInvalidInputResponsesExampleOne,
                                              summary = "Некорректный комментарий"),
                               @ExampleObject(name = "Запрашиваемые пост и/или комментарий"
                                              + " не существуют",
                                              value = addPostCommentInvalidInputResponsesExampleTwo,
                                              summary = "Неверный параметр на входе")})),
      @ApiResponse(responseCode = "401",
                   description = "Пользователь не аутентифицирован",
                   content = @Content(mediaType = "application/json"))
  })
  ResponseEntity<AddPostCommentRs> addPostComment(@RequestBody AddPostCommentRq addPostCommentRq);

  @PostMapping("/moderation")
  @PreAuthorize("hasAuthority('user:moderate')")
  @Operation(summary = "Модерация поста")
  @ApiResponses({
      @ApiResponse(responseCode = "200",
                   description = "Ответ в случае успешной или неуспешной модерации поста",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = ModerationPostRs.class)))
  })
  ResponseEntity<ModerationPostRs> moderatePost(@RequestBody ModerationPostRq moderationPostRq);

  String addPostCommentInvalidInputResponsesExampleOne = "{\n"
          + "  \"result\": false,\n"
          + "  \"errors\": \"ошибка: описание причины ошибки\"\n"
          + "}";

  String addPostCommentInvalidInputResponsesExampleTwo = "{\n"
          + "  \"message\": \"описание ошибки\"\n"
          + "}";
}