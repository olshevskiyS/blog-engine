package ru.olshevskiy.blogengine.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.olshevskiy.blogengine.dto.response.CalendarRs;
import ru.olshevskiy.blogengine.dto.response.GlobalSettingsRs;
import ru.olshevskiy.blogengine.dto.response.InitRs;
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
}
