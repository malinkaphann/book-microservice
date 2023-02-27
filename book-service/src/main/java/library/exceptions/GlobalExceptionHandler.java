/**
 * To make the controller's code clean and more readable, that's why
 * this class.
 *
 * @author Phann Malinka
 */
package library.exceptions;

import library.dto.ApiResponseDto;
import library.utils.AttributeUtil;
import library.utils.StatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private final Logger logger = LoggerFactory.getLogger(
    GlobalExceptionHandler.class
  );

  /**
   *
   * @param request
   * @return
   */
  private String getRequestId(WebRequest request) {
    Object attribute = request.getAttribute(AttributeUtil.REQUEST_ID, 0);
    String requestId = attribute != null ? attribute.toString() : "";
    return requestId;
  }

  /**
   *
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  protected ResponseEntity<ApiResponseDto> handleResourceNotFound(
    ResourceNotFoundException ex,
    WebRequest request
  ) {
    String requestId = getRequestId(request);
    logger.error(
      String.format(
        "request id = %s, message = %s",
        requestId,
        ex.getMessage()
      ),
      ex
    );
    return new ResponseEntity<ApiResponseDto>(
      new ApiResponseDto(ex.getStatusCode(), 
        String.format("ResourceNotFoundError: %s", ex.getMessage()), 
        requestId),
      HttpStatus.NOT_FOUND
    );
  }

  /**
   *
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(ResourceDuplicatedException.class)
  protected ResponseEntity<ApiResponseDto> handleResourceNotFound(
    ResourceDuplicatedException ex,
    WebRequest request
  ) {
    String requestId = getRequestId(request);
    logger.error(
      String.format(
        "request id = %s, message = %s",
        requestId,
        ex.getMessage()
      ),
      ex
    );
    return new ResponseEntity<ApiResponseDto>(
      new ApiResponseDto(ex.getStatusCode(), 
        String.format("ResourceDuplicatedError: %s", ex.getMessage()),
        requestId),
      HttpStatus.BAD_REQUEST
    );
  }

  /**
   *
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(ValidationException.class)
  protected ResponseEntity<ApiResponseDto> handleValidationError(
    ValidationException ex,
    WebRequest request
  ) {
    String requestId = getRequestId(request);
    logger.error(
      String.format(
        "request id = %s, message = %s",
        requestId,
        ex.getMessage()
      ),
      ex
    );
    return new ResponseEntity<ApiResponseDto>(
      new ApiResponseDto(ex.getStatusCode(), 
        String.format("ValidationError: %s", ex.getMessage()), 
        requestId),
      HttpStatus.BAD_REQUEST
    );
  }

  /**
   *
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(DatabaseException.class)
  protected ResponseEntity<ApiResponseDto> handleDatabaseError(
    DatabaseException ex,
    WebRequest request
  ) {
    String requestId = getRequestId(request);
    logger.error(
      String.format(
        "request id = %s, message = %s",
        requestId,
        ex.getMessage()
      ),
      ex
    );
    return new ResponseEntity<ApiResponseDto>(
      new ApiResponseDto(ex.getStatusCode(), 
        String.format("DatabaseError: %s", ex.getMessage()), 
        requestId),
      HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  /**
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiResponseDto> handleBadCredential(
    BadCredentialsException ex,
    WebRequest request
  ) {
    String requestId = getRequestId(request);
    logger.error(
      String.format(
        "request id = %s, message = %s",
        requestId,
        ex.getMessage()
      ),
      ex
    );
    return new ResponseEntity<ApiResponseDto>(
      new ApiResponseDto(
        StatusEnum.ERROR_UNEXPECTED.getValue(),
        "Error: login fails",
        requestId
      ),
      HttpStatus.BAD_REQUEST
    );
  }

  /**
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(UnsupportedOperationException.class)
  public ResponseEntity<ApiResponseDto> handleUnsupported(
    UnsupportedOperationException ex,
    WebRequest request
  ) {
    String requestId = getRequestId(request);
    logger.error(
      String.format(
        "request id = %s, message = %s",
        requestId,
        ex.getMessage()
      ),
      ex
    );
    return new ResponseEntity<ApiResponseDto>(
      new ApiResponseDto(
        StatusEnum.ERROR_UNEXPECTED.getValue(),
        "Error: unsupported yet",
        requestId
      ),
      HttpStatus.BAD_REQUEST
    );
  }
  
  /**
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponseDto> handleAnythingElse(
    Exception ex,
    WebRequest request
  ) {
    String requestId = getRequestId(request);
    logger.error(
      String.format(
        "request id = %s, message = %s",
        requestId,
        ex.getMessage()
      ),
      ex
    );
    return new ResponseEntity<ApiResponseDto>(
      new ApiResponseDto(
        StatusEnum.ERROR_UNEXPECTED.getValue(),
        String.format("Error: %s", ex.getMessage()),
        requestId
      ),
      HttpStatus.INTERNAL_SERVER_ERROR
    );
  }
}
