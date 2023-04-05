package ru.olshevskiy.blogengine.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

/**
 * EditProfileWithPhotoRq.
 *
 * @author Sergey Olshevskiy
 */
@Data
@JsonInclude(Include.NON_NULL)
@Accessors(chain = true)
@Schema(description = "Данные (с фото) для редактирования профиля пользователя")
public class EditProfileWithPhotoRq {

  @Schema(description = "Файл с фото")
  private MultipartFile photo;

  @Size(min = 2)
  @Schema(description = "Имя пользователя", example = "Сергей")
  private String name;

  @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
  @Schema(description = "Email пользователя", example = "user@xxx.ru")
  private String email;

  @Size(min = 6)
  @Schema(description = "Пароль пользователя", example = "1234567")
  private String password;

  @Schema(description = "Параметр указывающий удалить фото или нет", example = "0")
  private int removePhoto;
}