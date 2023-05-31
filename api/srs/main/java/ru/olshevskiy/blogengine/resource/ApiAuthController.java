package ru.olshevskiy.blogengine.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.olshevskiy.blogengine.dto.Error;
import ru.olshevskiy.blogengine.dto.IncorrectCredentialsDto;
import ru.olshevskiy.blogengine.dto.InvalidInput;
import ru.olshevskiy.blogengine.dto.request.ChangePassRq;
import ru.olshevskiy.blogengine.dto.request.LoginRq;
import ru.olshevskiy.blogengine.dto.request.RegistrationRq;
import ru.olshevskiy.blogengine.dto.request.RestorePassRq;
import ru.olshevskiy.blogengine.dto.response.CaptchaRs;
import ru.olshevskiy.blogengine.dto.response.ChangePassRs;
import ru.olshevskiy.blogengine.dto.response.LoginAndCheckRs;
import ru.olshevskiy.blogengine.dto.response.LogoutRs;
import ru.olshevskiy.blogengine.dto.response.RegistrationRs;
import ru.olshevskiy.blogengine.dto.response.RestorePassRs;

/**
 * ApiAuthController.
 *
 * @author Sergey Olshevskiy
 */
@RequestMapping("/api/auth")
@Tag(name = "Authentication Service", description = "Работа с аутентификацией и авторизацией")
public interface ApiAuthController {

  @GetMapping("/check")
  @Operation(summary = "Проверка статуса аутентификации")
  @ApiResponse(responseCode = "200",
               description = "Успешная проверка",
               content = @Content(mediaType = "application/json",
               schema = @Schema(oneOf = {LoginAndCheckRs.class, IncorrectCredentialsDto.class}),
               examples = {@ExampleObject(name = "Ответ сервера в случае "
                                          + "если пользователь аутентифицирован",
                                          value = loginAndCheckResponsesExampleOne,
                                          summary = "Пользователь аутентифицирован"),
                           @ExampleObject(name = "Ответ сервера в случае "
                                          + "если пользователь не аутентифицирован",
                                          value = loginAndCheckResponsesExampleTwo,
                                          summary = "Пользователь не аутентифицирован")})
  )
  ResponseEntity<LoginAndCheckRs> check();

  @PostMapping("/register")
  @Operation(summary = "Регистрация нового пользователя")
  @ApiResponses({
      @ApiResponse(responseCode = "201",
                   description = "Успешная регистрация",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = RegistrationRs.class))),
      @ApiResponse(responseCode = "400",
                   description = "Введены неверные данные",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = Error.class))),
      @ApiResponse(responseCode = "404",
                   description = "Регистрация пользователей недоступна",
                   content = @Content(mediaType = "application/json"))
  })
  ResponseEntity<RegistrationRs> register(@Valid @RequestBody RegistrationRq registrationRq);

  @GetMapping("/captcha")
  @Operation(summary = "Запрос каптчи")
  @ApiResponse(responseCode = "200",
               description = "Успешный запрос",
               content = @Content(mediaType = "application/json",
               schema = @Schema(implementation = CaptchaRs.class)))
  ResponseEntity<CaptchaRs> getCaptcha();

  @PostMapping("/login")
  @Operation(summary = "Процедура аутентификации и авторизации пользователя")
  @ApiResponse(responseCode = "200",
               description = "Запрос на вход в систему",
               content = @Content(mediaType = "application/json",
               schema = @Schema(oneOf = {LoginAndCheckRs.class, IncorrectCredentialsDto.class}),
               examples = {@ExampleObject(name = "Ответ сервера в случае успешного входа в систему",
                                          value = loginAndCheckResponsesExampleOne,
                                          summary = "Успешный вход в систему"),
                           @ExampleObject(name = "Ответ сервера в случае ошибки учетных данных",
                                          value = loginAndCheckResponsesExampleTwo,
                                          summary = "Неверные учетные данные")})
  )
  ResponseEntity<LoginAndCheckRs> login(@RequestBody LoginRq loginRq, HttpServletRequest request,
                                        HttpServletResponse response);

  @GetMapping("/logout")
  @Operation(summary = "Процедура выхода пользователя из системы")
  @ApiResponse(responseCode = "200",
               description = "Успешный запрос",
               content = @Content(mediaType = "application/json",
               schema = @Schema(implementation = LogoutRs.class)))
  ResponseEntity<LogoutRs> logout(HttpServletRequest request, HttpServletResponse response);

  @PostMapping("/restore")
  @Operation(summary = "Восстановление пароля")
  @ApiResponses({
      @ApiResponse(responseCode = "200",
                   description = "Ссылка восстановления отправлена или логин не найден",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = RestorePassRs.class))),
      @ApiResponse(responseCode = "400",
                   description = "Неккоректный формат почтового адреса",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = InvalidInput.class)))
  })
  ResponseEntity<RestorePassRs> restorePassword(@RequestBody RestorePassRq restorePassRq);

  @PostMapping("/password")
  @Operation(summary = "Изменение пароля")
  @ApiResponses({
      @ApiResponse(responseCode = "200",
                   description = "Пароль успешно изменен",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = ChangePassRs.class))),
      @ApiResponse(responseCode = "400",
                   description = "Введены неверные данные или истекло время действия ссылки",
                   content = @Content(mediaType = "application/json",
                   schema = @Schema(implementation = Error.class)))
  })
  ResponseEntity<ChangePassRs> changePassword(@RequestBody ChangePassRq changePassRq);

  String loginAndCheckResponsesExampleOne = "{\n"
          + "    \"result\": true,\n"
          + "    \"user\": {\n"
          + "        \"id\": 5,\n"
          + "        \"name\": \"Сергей\",\n"
          + "        \"photo\": null,\n"
          + "        \"email\": \"user@xxx.ru\",\n"
          + "        \"moderation\": true,\n"
          + "        \"moderationCount\": 8,\n"
          + "        \"settings\": true\n"
          + "    }\n"
          + "}";

  String loginAndCheckResponsesExampleTwo = "{\n"
          + "  \"result\": false\n"
          + "}";
}
