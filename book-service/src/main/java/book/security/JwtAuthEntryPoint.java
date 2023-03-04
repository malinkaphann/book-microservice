/**
 * This is to handle the unauthorized requests.
 * This is just to make the error response to conform with a standard
 * response dto.
 * 
 * @author Phann Malinka
 */
package book.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import book.dto.ApiResponseDto;
import book.utils.AttributeUtil;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {
                
        logger.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final ObjectMapper mapper = new ObjectMapper();
         
        ApiResponseDto responseDto = new ApiResponseDto(
            HttpServletResponse.SC_UNAUTHORIZED,
            "Unauthorized",
            MDC.get(AttributeUtil.REQUEST_ID)
        );

        mapper.writeValue(response.getOutputStream(), responseDto);
    }
}