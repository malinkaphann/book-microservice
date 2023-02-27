/**
 * This is the profile update request dto.
 * 
 * @author Phann Malinka
 */
package library.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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