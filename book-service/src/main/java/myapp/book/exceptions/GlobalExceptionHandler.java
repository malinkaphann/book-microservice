/**
 * This is to handle the exceptions globally.
 *
 * @author Phann Malinka
 */
package myapp.book.exceptions;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import myapp.book.dto.ApiResponseDto;
import myapp.book.utils.AttributeUtil;
import myapp.book.utils.StatusEnum;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private final Logger logger = LoggerFactory.getLogger(
      GlobalExceptionHandler.class);

  /**
   * This is to handle the ResourceNotFoundException
   * 
   * @param ex the ResourceNotFoundException
   * @param request the web request
   * @return ApiResponseDto
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  protected ResponseEntity<ApiResponseDto> handleResourceNotFound(
      ResourceNotFoundException ex,
      WebRequest request) {
    logger.error(ex.getMessage(), ex);
    String requestId = MDC.get(AttributeUtil.REQUEST_ID);
    requestId = requestId == null ? "unknown" : requestId.toString();
    return new ResponseEntity<ApiResponseDto>(
        new ApiResponseDto(ex.getStatus().getValue(),
            String.format("ResourceNotFoundError: %s", ex.getMessage()),
            requestId),
        HttpStatus.NOT_FOUND);
  }

  /**
   * This is the handle the ResourceDuplicatedException.
   * 
   * @param ex the ResourceDuplicatedException
   * @param request the web request
   * @return ApiResponseDto
   */
  @ExceptionHandler(ResourceDuplicatedException.class)
  protected ResponseEntity<ApiResponseDto> handleResourceDuplicated(
      ResourceDuplicatedException ex,
      WebRequest request) {
    logger.error(ex.getMessage(), ex);
    String requestId = MDC.get(AttributeUtil.REQUEST_ID);
    requestId = requestId == null ? "unknown" : requestId.toString();
    return new ResponseEntity<ApiResponseDto>(
        new ApiResponseDto(ex.getStatus().getValue(),
            String.format("ResourceDuplicatedError: %s", ex.getMessage()),
            requestId),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * This is handle the ValidationException.
   * 
   * @param ex the ValidationException
   * @param request the web request
   * @return ApiResponseDto
   */
  @ExceptionHandler(ValidationException.class)
  protected ResponseEntity<ApiResponseDto> handleValidationError(
      ValidationException ex,
      WebRequest request) {
    logger.error(ex.getMessage(), ex);
    String requestId = MDC.get(AttributeUtil.REQUEST_ID);
    requestId = requestId == null ? "unknown" : requestId.toString();
    return new ResponseEntity<ApiResponseDto>(
        new ApiResponseDto(ex.getStatus().getValue(),
            String.format("ValidationError: %s", ex.getMessage()),
            requestId),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * This is handle the DatabaseException.
   * 
   * @param ex the DatabaseException
   * @param request the web request
   * @return ApiResponseDto
   */
  @ExceptionHandler(DatabaseException.class)
  protected ResponseEntity<ApiResponseDto> handleDatabaseError(
      DatabaseException ex,
      WebRequest request) {
    logger.error(ex.getMessage(), ex);
    String requestId = MDC.get(AttributeUtil.REQUEST_ID);
    requestId = requestId == null ? "unknown" : requestId.toString();
    return new ResponseEntity<ApiResponseDto>(
        new ApiResponseDto(ex.getStatus().getValue(),
            String.format("DatabaseError: %s", ex.getMessage()),
            requestId),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * This is to handle the BadCredentialsException that is thrown
   * when the login fails.
   * 
   * @param ex the BadCredentialsException
   * @param request the web request
   * @return ApiResponseDto
   */
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiResponseDto> handleBadCredential(
      BadCredentialsException ex,
      WebRequest request) {
    logger.error(ex.getMessage(), ex);
    String requestId = MDC.get(AttributeUtil.REQUEST_ID);
    requestId = requestId == null ? "unknown" : requestId.toString();
    return new ResponseEntity<ApiResponseDto>(
        new ApiResponseDto(
            StatusEnum.ERROR_UNEXPECTED.getValue(),
            "Login fails",
            requestId),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * This is to handle any error coming from validating the request dto.
   * 
   * @param ex the MethodArgumentNotValidException
   * @param headers
   * @param status
   * @param request
   * @return ApiResponseDto
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
    
    logger.error(ex.getMessage(), ex);

    List<String> errors = new ArrayList<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getDefaultMessage());
    }

    String requestId = MDC.get(AttributeUtil.REQUEST_ID);
    requestId = requestId == null ? "unknown" : requestId.toString();

    ApiResponseDto response = new ApiResponseDto(
        StatusEnum.ERROR_VALIDATION.getValue(), errors.get(0), 
        requestId);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * This is to handle the error coming from the malformatted json request.
   * 
   * @param ex HttpMessageNotReadableException
   * @param headers
   * @param status
   * @param request
   */
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex, HttpHeaders headers, 
      HttpStatus status, WebRequest request) {

    logger.error(ex.getMessage(), ex);

    String requestId = MDC.get(AttributeUtil.REQUEST_ID);
    requestId = requestId == null ? "unknown" : requestId.toString();

    ApiResponseDto response = new ApiResponseDto(
        StatusEnum.ERROR_INVALID_JSON.getValue(), 
        "invalid json request",
        requestId);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * This is to handle the UnsupportedOperationException.
   * 
   * @param ex UnsupportedOperationException
   * @param request the web request
   * @return ApiResponseDto
   */
  @ExceptionHandler(UnsupportedOperationException.class)
  public ResponseEntity<ApiResponseDto> handleUnsupported(
      UnsupportedOperationException ex,
      WebRequest request) {
    logger.error(ex.getMessage(), ex);
    String requestId = MDC.get(AttributeUtil.REQUEST_ID);
    requestId = requestId == null ? "unknown" : requestId.toString();
    return new ResponseEntity<ApiResponseDto>(
        new ApiResponseDto(
            StatusEnum.ERROR_UNEXPECTED.getValue(),
            "Error: unsupported yet",
            requestId),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * This is to handle everything else.
   * 
   * @param ex the Exception
   * @param request the web request
   * @return ApiResponseDto
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponseDto> handleAnythingElse(
      Exception ex,
      WebRequest request) {
    logger.error(ex.getMessage(), ex);
    String requestId = MDC.get(AttributeUtil.REQUEST_ID);
    requestId = requestId == null ? "unknown" : requestId.toString();
    return new ResponseEntity<ApiResponseDto>(
        new ApiResponseDto(
            StatusEnum.ERROR_UNEXPECTED.getValue(),
            String.format("Error: %s", ex.getMessage()),
            requestId),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
