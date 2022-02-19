package ru.olshevskiy.blogengine.controller;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.olshevskiy.blogengine.model.dto.GlobalSettingDto;
import ru.olshevskiy.blogengine.model.dto.InitDto;
import ru.olshevskiy.blogengine.model.dto.TagDto;
import ru.olshevskiy.blogengine.service.GlobalSettingServiceImpl;
import ru.olshevskiy.blogengine.service.TagServiceImpl;

/**
 * Контроллер запросов к API.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiGeneralController {

  private final InitDto initDto;
  private final GlobalSettingServiceImpl settingsService;
  private final TagServiceImpl tagService;

  @GetMapping("/init")
  private ResponseEntity<InitDto> init() {
    return new ResponseEntity<>(initDto, HttpStatus.OK);
  }

  @GetMapping("/settings")
  private ResponseEntity<GlobalSettingDto> getBlogGlobalSettings() {
    return new ResponseEntity<>(settingsService.getGlobalSettings(), HttpStatus.OK);
  }

  @GetMapping("/tag")
  private ResponseEntity<Map<String, List<TagDto>>> getTagsByQuery(
          @RequestParam(required = false) String query) {
    return new ResponseEntity<>(tagService.getTagsByQuery(query), HttpStatus.OK);
  }
}
