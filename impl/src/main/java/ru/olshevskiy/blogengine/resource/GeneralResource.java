package ru.olshevskiy.blogengine.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.olshevskiy.blogengine.dto.response.CalendarRs;
import ru.olshevskiy.blogengine.dto.response.GlobalSettingsRs;
import ru.olshevskiy.blogengine.dto.response.InitRs;
import ru.olshevskiy.blogengine.dto.response.TagsByQueryRs;
import ru.olshevskiy.blogengine.service.GeneralService;
import ru.olshevskiy.blogengine.service.TagService;

/**
 * GeneralResource.
 *
 * @author Sergey Olshevskiy
 */
@RestController
@RequiredArgsConstructor
public class GeneralResource implements ApiGeneralController {

  private final InitRs initRs;
  private final GeneralService generalService;
  private final TagService tagService;

  @Override
  public ResponseEntity<InitRs> init() {
    return new ResponseEntity<>(initRs, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<GlobalSettingsRs> getGlobalSettings() {
    return new ResponseEntity<>(generalService.getGlobalSettings(), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<TagsByQueryRs> getTagsByQuery(String query) {
    return new ResponseEntity<>(tagService.getTagsByQuery(query), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<CalendarRs> getCalendar(String year) {
    return new ResponseEntity<>(generalService.getCalendar(year), HttpStatus.OK);
  }
}
