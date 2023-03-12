/**
 * This is the log filter that intercepts the requests and the responses.
 * The idea is to log and stamp every request/response by giving a unique
 * id to the request.
 *
 * @author Phann Malinka
 */
package myapp.book.filters;

import myapp.book.utils.AttributeUtil;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LogFilter {

  private static final Logger logger = LoggerFactory.getLogger(LogFilter.class);

  protected void doFilterInternal(
    HttpServletRequest req,
    HttpServletResponse res,
    FilterChain chain
  ) throws ServletException, IOException {
    try {
      HttpServletRequest request = (HttpServletRequest) req;
      HttpServletResponse response = (HttpServletResponse) res;

      // generate the request id and put in MDC
      String requestId = UUID.randomUUID().toString();
      MDC.put(AttributeUtil.REQUEST_ID, requestId);

      String ipAddress = request.getHeader("X-FORWARDED-FOR");
      if (ipAddress == null) {
        ipAddress = request.getRemoteAddr();
      }

      String queryString = request.getQueryString();
      String uri = queryString == null
        ? request.getRequestURI()
        : String.format("%s?%s", request.getRequestURI(), queryString);

      // log the request
      logger.debug(
        "REQUEST: ip = {}, uri ={}, method = {}",
        ipAddress,
        uri,
        request.getMethod()
      );

      chain.doFilter(request, response);
      
    } catch (Exception e) {} finally {
      MDC.remove(AttributeUtil.REQUEST_ID);
    }
  }
}
