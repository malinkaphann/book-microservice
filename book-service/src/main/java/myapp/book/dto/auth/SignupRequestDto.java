/**
 * This is the sign up request dto.
 *
 * @author Phann Malinka
 */
package myapp.book.dto.auth;

import org.apache.commons.validator.routines.EmailValidator;
import myapp.book.exceptions.ValidationException;
import myapp.book.utils.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
  private String username;
  
  @ToString.Exclude
  private String password;
  private String studentId;
  private String name;
  private String phone;
  private String email;

  // csv
  private String roles;

  public void validate() {

    // validate username
    if (this.username == null) {
      throw new ValidationException("username is required");
    }

    ValidationUtil.validateSize(
      "username",
      this.username,
      ValidationUtil.MIN_LEN_USERNAME,
      ValidationUtil.MAX_LEN_USERNAME
    );

    // validate password
    if (this.password == null) {
      throw new ValidationException("password is required");
    }

    ValidationUtil.validateSize(
      "password",
      this.password,
      ValidationUtil.MIN_LEN_PASSWORD,
      ValidationUtil.MAX_LEN_PASSWORD
    );

    // validate student id
    if (this.studentId != null) {
    ValidationUtil.validateSize("studentId", this.studentId, 
        ValidationUtil.MIN_LEN_STUDENT_ID, ValidationUtil.MAX_LEN_STUDENT_ID);
    }
    
    // validate name
    if (this.name == null || this.name.isEmpty()) {
        throw new ValidationException("name is required");
    }
    
    ValidationUtil.validateSize("name", this.name, 
        ValidationUtil.MIN_LEN_NAME, ValidationUtil.MAX_LEN_NAME);

    // validate phone
    if (this.phone == null || this.phone.isEmpty()) {
        throw new ValidationException("phone is required");
    }

    ValidationUtil.validateSize("phone", this.phone, 
        ValidationUtil.MIN_LEN_PHONE, ValidationUtil.MAX_LEN_PHONE);
    
    // validate email
    if (this.email == null || this.email.isEmpty()) {
        throw new ValidationException("email is required");
    }

    if (!EmailValidator.getInstance().isValid(this.email)) {
        throw new ValidationException(String.format(
            "email = %s is not valid", this.email));
    }

    // validate roles
    if (this.roles == null || this.roles.isEmpty()) {
        throw new ValidationException("roles is required");
    }
  }
}
