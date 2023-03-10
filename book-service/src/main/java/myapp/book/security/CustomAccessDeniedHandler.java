/*
 * This is the custom access denied handler.
 *
 * @author Phann Malinka
 */
package myapp.book.security;

import myapp.book.dto.ApiResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final Logger logger = LoggerFactory.getLogger(
    CustomAccessDeniedHandler.class
  );

  @Override
  public void handle(
    HttpServletRequest request,
    HttpServletResponse response,
    AccessDeniedException ex
  ) throws IOException, ServletException {

    // generate the request id and put in MDC
    String requestId = UUID.randomUUID().toString();

    logger.error("ForbiddenError: {}", ex);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);

    final ObjectMapper mapper = new ObjectMapper();

    ApiResponseDto responseDto = new ApiResponseDto(
      HttpServletResponse.SC_FORBIDDEN,
      "Forbidden",
      requestId
    );

    mapper.writeValue(response.getOutputStream(), responseDto);
  }
}
