package ru.olshevskiy.blogengine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.olshevskiy.blogengine.model.dto.response.CalendarRs;
import ru.olshevskiy.blogengine.model.dto.response.GlobalSettingsRs;
import ru.olshevskiy.blogengine.model.dto.response.InitRs;
import ru.olshevskiy.blogengine.model.dto.response.TagsByQueryRs;
import ru.olshevskiy.blogengine.service.GeneralServiceImpl;
import ru.olshevskiy.blogengine.service.TagServiceImpl;

/**
 * ApiGeneralController.
 *
 * @author Sergey Olshevskiy
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiGeneralController {

  private final InitRs initRs;
  private final GeneralServiceImpl generalResource;
  private final TagServiceImpl tagResource;

  @GetMapping("/init")
  public ResponseEntity<InitRs> init() {
    return new ResponseEntity<>(initRs, HttpStatus.OK);
  }

  @GetMapping("/settings")
  public ResponseEntity<GlobalSettingsRs> getBlogGlobalSettings() {
    return new ResponseEntity<>(generalResource.getGlobalSettings(), HttpStatus.OK);
  }

  @GetMapping("/tag")
  public ResponseEntity<TagsByQueryRs> getTagsByQuery(
          @RequestParam(required = false) String query) {
    return new ResponseEntity<>(tagResource.getTagsByQuery(query), HttpStatus.OK);
  }

  @GetMapping("/calendar")
  public ResponseEntity<CalendarRs> getCalendar(@RequestParam(required = false) String year) {
    return new ResponseEntity<>(generalResource.getCalendar(year), HttpStatus.OK);
  }
}
