package ru.olshevskiy.blogengine.model.dto;

/**
 * ErrorDescription.
 *
 * @author Sergey Olshevskiy
 */
public class ErrorDescription {

  public static String INCORRECT_EMAIL_FORMAT = "Некорректный формат e-mail";
  public static String EMAIL_DUPLICATE = "Этот e-mail уже зарегистрирован";
  public static String PASSWORD_LENGTH_TOO_SMALL = "Пароль короче %d-ти символов";
  public static String NAME_LENGTH_TOO_SMALL = "Введенное имя слишком короткое";
  public static String INVALID_CAPTCHA_CODE = "Код с картинки введен неверно";
  public static String USER_NOT_FOUND = "Пользователь с e-mail %s не найден";
}