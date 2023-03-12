/**
 * This is the sign up request dto.
 *
 * @author Phann Malinka
 */
package myapp.book.dto.auth;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import myapp.book.entities.Role;
import myapp.book.utils.ValidationUtil;
import lombok.Data;
import lombok.ToString;

@Data
public class SignupRequestDto {

  @NotEmpty(message = "username is required")
  @Size(min = ValidationUtil.MIN_LEN_USERNAME, max = ValidationUtil.MAX_LEN_USERNAME, message = "username must be between {min} and {max} characters")
  private String username;

  @Size(min = ValidationUtil.MIN_LEN_PASSWORD, max = ValidationUtil.MAX_LEN_PASSWORD, message = "password must be between {min} and {max} characters")
  @ToString.Exclude
  private String password;

  @Size(max = ValidationUtil.MAX_LEN_STUDENT_ID, message = "studentId must not be longer than {max} characters")
  private String studentId;

  @NotEmpty(message = "name is required")
  @Size(min = ValidationUtil.MIN_LEN_NAME, max = ValidationUtil.MAX_LEN_NAME, message = "name must be between {min} and {max} characters")
  private String name;

  @NotEmpty(message = "phone is required")
  @Size(min = ValidationUtil.MIN_LEN_PHONE, max = ValidationUtil.MAX_LEN_PHONE, message = "phone must be between {min} and {max} characters")
  private String phone;

  @NotEmpty(message = "email is required")
  @Email(message = "email = ${validatedValue} is not a valid email")
  private String email;

  @NotEmpty(message = "roles is required")
  private List<Role> roles = new ArrayList<>();
}
