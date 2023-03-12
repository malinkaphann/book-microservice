/**
 * This is the login request dto.
 * 
 * @author Phann Malinka
 */
package myapp.book.dto.auth;

import myapp.book.utils.ValidationUtil;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
public class LoginRequestDto {

  @NotEmpty(message = "username is required")
  @Size(max = ValidationUtil.MAX_LEN_USERNAME, 
    message = "the username = '${validatedValue}' must be shorter than {max} characters")
  String username;

  @ToString.Exclude
  @NotEmpty(message = "password is required")
  @Size(max = ValidationUtil.MAX_LEN_PASSWORD, 
    message = "the password must be shorter than {max} characters")
  String password;
}
