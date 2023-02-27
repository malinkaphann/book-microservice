package library.dto.auth;

import library.dto.ApiRequestDto;
import library.exceptions.ValidationException;
import library.utils.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto extends ApiRequestDto {

  private String username;
  private String password;

  public void validate() {
    if (this.username == null) {
      throw new ValidationException("username is required");
    }

    ValidationUtil.validateSize(
      "username",
      this.username,
      ValidationUtil.MIN_LEN_USERNAME,
      ValidationUtil.MAX_LEN_USERNAME
    );

    if (this.password == null) {
      throw new ValidationException("password is required");
    }

    ValidationUtil.validateSize(
      "password",
      this.password,
      ValidationUtil.MIN_LEN_PASSWORD,
      ValidationUtil.MAX_LEN_PASSWORD
    );
  }

  public String toString() {
    return String.format(
      "LoginRequestDto(request id = %s, " + "username = %s, password = MASKED)",
      super.requestId,
      username
    );
  }
}
