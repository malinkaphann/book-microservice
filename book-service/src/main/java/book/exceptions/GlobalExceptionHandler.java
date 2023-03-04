/**
 * To make the controller's code clean and more readable, that's why
 * this class.
 *
 * @author Phann Malinka
 */
package book.exceptions;

import org.jboss.logging.MDC;
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
import book.dto.ApiResponseDto;
import book.utils.AttributeUtil;
import book.utils.StatusEnum;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private final Logger logger = LoggerFactory.getLogger(
    GlobalExceptionHandler.class
  );

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
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<ApiResponseDto>(
      new ApiResponseDto(ex.getStatus().getValue(), 
        String.format("ResourceNotFoundError: %s", ex.getMessage()), 
        MDC.get(AttributeUtil.REQUEST_ID).toString()),
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
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<ApiResponseDto>(
      new ApiResponseDto(ex.getStatus().getValue(), 
        String.format("ResourceDuplicatedError: %s", ex.getMessage()),
        MDC.get(AttributeUtil.REQUEST_ID).toString()),
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
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<ApiResponseDto>(
      new ApiResponseDto(ex.getStatus().getValue(), 
        String.format("ValidationError: %s", ex.getMessage()), 
        MDC.get(AttributeUtil.REQUEST_ID).toString()),
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
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<ApiResponseDto>(
      new ApiResponseDto(ex.getStatus().getValue(), 
        String.format("DatabaseError: %s", ex.getMessage()), 
        MDC.get(AttributeUtil.REQUEST_ID).toString()),
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
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<ApiResponseDto>(
      new ApiResponseDto(
        StatusEnum.ERROR_UNEXPECTED.getValue(),
        "Login fails",
        MDC.get(AttributeUtil.REQUEST_ID).toString()),
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
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<ApiResponseDto>(
      new ApiResponseDto(
        StatusEnum.ERROR_UNEXPECTED.getValue(),
        "Error: unsupported yet",
        MDC.get(AttributeUtil.REQUEST_ID).toString()),
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
    logger.error(ex.getMessage(), ex);
    return new ResponseEntity<ApiResponseDto>(
      new ApiResponseDto(
        StatusEnum.ERROR_UNEXPECTED.getValue(),
        String.format("Error: %s", ex.getMessage()),
        MDC.get(AttributeUtil.REQUEST_ID).toString()),
      HttpStatus.INTERNAL_SERVER_ERROR
    );
  }
}
