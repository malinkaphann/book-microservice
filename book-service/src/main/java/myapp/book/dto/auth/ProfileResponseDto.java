/**
 * This is the profile update request dto.
 * 
 * @author Phann Malinka
 */
package myapp.book.dto.auth;

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
public class ProfileResponseDto {
    String username;
    String name;
    String phone;
    String email;
    String photo;
    String studentId;
}