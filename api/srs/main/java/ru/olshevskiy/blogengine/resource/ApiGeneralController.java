package ru.olshevskiy.blogengine.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.olshevskiy.blogengine.dto.Error;
import ru.olshevskiy.blogengine.dto.request.EditProfileWithPhotoRq;
import ru.olshevskiy.blogengine.dto.request.EditProfileWithoutPhotoRq;
import ru.olshevskiy.blogengine.dto.response.AllStatisticsRs;
import ru.olshevskiy.blogengine.dto.response.CalendarRs;
import ru.olshevskiy.blogengine.dto.response.EditProfileRs;
import ru.olshevskiy.blogengine.dto.response.GlobalSettingsRs;
import ru.olshevskiy.blogengine.dto.response.InitRs;
import ru.olshevskiy.blogengine.dto.response.MyStatisticsRs;
import ru.olshevskiy.blogengine.dto.response.TagsByQueryRs;

/**
 * ApiGeneralController.
 *
 * @author Sergey Olshevskiy
 */
@RequestMapping("/api")
@Tag(name = "General Service", description = "Работа с ресурсами блога")
public interface ApiGeneralController {

  @GetMapping("/init")
  @Operation(summary = "Запрос общей информации о блоге")
  @ApiResponse(responseCode = "200",
               description = "Успешный запрос",
               content = @Content(mediaType = "application/json",
               schema = @Schema(implementation = InitRs.class)))
  ResponseEntity<InitRs> init();

  @GetMapping("/settings")
  @Operation(summary = "Получение настроек блога")
  @ApiResponse(responseCode = "200",
               description = "Успешный запрос",
               content = @Content(mediaType = "application/json",
               schema = @Schema(implementation = GlobalSettingsRs.class)))
  ResponseEntity<GlobalSettingsRs> getGlobalSettings();

  @GetMapping("/tag")
  @Operation(summary = "Получение списка тэгов")
  @ApiResponse(responseCode = "200",
               description = "Успешный запрос",
               content = @Content(mediaType = "application/json",
               schema = @Schema(implementation = TagsByQueryRs.class)))
  ResponseEntity<TagsByQueryRs> getTagsByQuery(@Parameter(description = "Часть тэга или тэг")
                                               @RequestParam(required = false) String query);

  @GetMapping("/calendar")
  @Operation(summary = "Календарь публикаций")
  @ApiResponse(responseCode = "200",
               description = "Успешный запрос",
               content = @Content(mediaType = "application/json",
               schema = @Schema(implementation = CalendarRs.class)))
  ResponseEntity<CalendarRs> getCalendar(@Parameter(description = "Заданный год")
                                         @RequestParam(required = false) String year);

  @PostMapping(value = "/profile/my",
               consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('user:write')")
  @Operation(summary = "Редактирование профиля текущего пользователя")
  @ApiResponses({
      @ApiResponse(responseCode = "200",
                   description = "Успешный запрос",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = EditProfileRs.class))),
      @ApiResponse(responseCode = "400",
                   description = "Введены неверные (некорректные) данные",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = Error.class)))
  })
  ResponseEntity<EditProfileRs> editProfileWithPhoto(@ModelAttribute @Valid
                                                     EditProfileWithPhotoRq editProfileRq);

  @PostMapping(value = "/profile/my",
               consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('user:write')")
  @Operation(summary = "Редактирование профиля текущего пользователя")
  @ApiResponses({
      @ApiResponse(responseCode = "200",
                   description = "Успешный запрос",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = EditProfileRs.class))),
      @ApiResponse(responseCode = "400",
                   description = "Введены неверные (некорректные) данные",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = Error.class)))
  })
  ResponseEntity<EditProfileRs> editProfileWithoutPhoto(@RequestBody @Valid
                                                        EditProfileWithoutPhotoRq editProfileRq);

  @GetMapping("/statistics/my")
  @PreAuthorize("hasAuthority('user:write')")
  @Operation(summary = "Моя статистика")
  @ApiResponse(responseCode = "200",
               description = "Успешный запрос",
               content = @Content(mediaType = "application/json",
               schema = @Schema(implementation = MyStatisticsRs.class)))
  ResponseEntity<MyStatisticsRs> getMyStatistics();

  @GetMapping("/statistics/all")
  @Operation(summary = "Статистика по всему блогу")
  @ApiResponses({
      @ApiResponse(responseCode = "200",
                   description = "Успешный запрос",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = AllStatisticsRs.class))),
      @ApiResponse(responseCode = "401",
                   description = "Пользователь не аутентифицирован",
                   content = @Content(mediaType = "application/json"))
  })
  ResponseEntity<AllStatisticsRs> getAllStatistics();
}