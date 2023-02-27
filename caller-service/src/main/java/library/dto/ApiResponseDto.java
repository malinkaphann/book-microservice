/**
 * This is an abstract top most parent class to generate the standardized response.
 *
 * @author Phann Malinka
 */
package library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto {
    private int status;
    private String message;
}