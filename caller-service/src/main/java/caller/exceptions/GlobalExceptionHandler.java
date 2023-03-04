/**
 * To make the controller's code clean and more readable, that's why
 * this class.
 *
 * @author Phann Malinka
 */
package caller.exceptions;

import caller.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

  /**
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(HttpClientErrorException.class)
  public ResponseEntity<ApiResponseDto> handleAnythingElse(
    HttpClientErrorException ex,
    WebRequest request
  ) {
    return new ResponseEntity<ApiResponseDto>(
      new ApiResponseDto(
        101,
        String.format("Error: %s", ex.getMessage())
      ),
      HttpStatus.INTERNAL_SERVER_ERROR
    );
  }
}
