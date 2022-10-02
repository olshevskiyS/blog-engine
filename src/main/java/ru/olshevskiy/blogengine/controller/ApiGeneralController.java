package ru.olshevskiy.blogengine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.olshevskiy.blogengine.model.dto.CalendarDto;
import ru.olshevskiy.blogengine.model.dto.GlobalSettingDto;
import ru.olshevskiy.blogengine.model.dto.InitDto;
import ru.olshevskiy.blogengine.model.dto.TagsByQueryDto;
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

  private final InitDto initDto;
  private final GeneralServiceImpl generalResource;
  private final TagServiceImpl tagResource;

  @GetMapping("/init")
  public ResponseEntity<InitDto> init() {
    return new ResponseEntity<>(initDto, HttpStatus.OK);
  }

  @GetMapping("/settings")
  public ResponseEntity<GlobalSettingDto> getBlogGlobalSettings() {
    return new ResponseEntity<>(generalResource.getGlobalSettings(), HttpStatus.OK);
  }

  @GetMapping("/tag")
  public ResponseEntity<TagsByQueryDto> getTagsByQuery(
          @RequestParam(required = false) String query) {
    return new ResponseEntity<>(tagResource.getTagsByQuery(query), HttpStatus.OK);
  }

  @GetMapping("/calendar")
  public ResponseEntity<CalendarDto> getCalendar(@RequestParam String year) {
    return new ResponseEntity<>(generalResource.getCalendar(year), HttpStatus.OK);
  }
}
