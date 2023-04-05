package ru.olshevskiy.blogengine.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * EditProfileWithoutPhotoRq.
 *
 * @author Sergey Olshevskiy
 */
@Data
@JsonInclude(Include.NON_NULL)
@Accessors(chain = true)
@Schema(description = "Данные (без фото) для редактирования профиля пользователя")
public class EditProfileWithoutPhotoRq {

  @Schema(description = "Пустое значение в случае, если фото нужно удалить")
  private String photo;

  @Size(min = 2)
  @Schema(description = "Новое имя пользователя", example = "Сергей")
  private String name;

  @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
  @Schema(description = "Новый email пользователя", example = "user@xxx.ru")
  private String email;

  @Size(min = 6)
  @Schema(description = "Новый пароль пользователя", example = "1234567")
  private String password;

  @Schema(description = "Параметр, указывающий удалить фото или нет", example = "1")
  private int removePhoto;
}