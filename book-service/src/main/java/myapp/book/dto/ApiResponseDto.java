/**
 * This is an abstract top most parent class to generate the standardized response.
 *
 * @author Phann Malinka
 */
package myapp.book.dto;

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
public class ApiResponseDto {
    protected int status;
    protected String message;
    protected String requestId;
}