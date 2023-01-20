package ru.olshevskiy.blogengine.dto;

/**
 * ErrorDescription.
 *
 * @author Sergey Olshevskiy
 */
public class ErrorDescription {

  public static String INCORRECT_EMAIL_FORMAT = "Некорректный формат e-mail";
  public static String EMAIL_DUPLICATE = "Этот e-mail уже зарегистрирован";
  public static String PASSWORD_LENGTH_TOO_SMALL = "Пароль короче %d символов";
  public static String NAME_LENGTH_TOO_SMALL = "Введенное имя слишком короткое";
  public static String INVALID_CAPTCHA_CODE = "Код с картинки введен неверно";
  public static String USER_NOT_FOUND = "Пользователь с e-mail %s не найден";
  public static String POST_TITLE_TOO_SMALL = "Заголовок поста не установлен или слишком короткий,"
                                              + " должен быть не менее %d символов";
  public static String POST_TEXT_TOO_SMALL = "Текст поста слишком короткий, "
                                             + "должен быть не менее %d символов";
}