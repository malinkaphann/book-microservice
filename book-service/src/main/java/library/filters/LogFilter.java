/**
 * This is the log filter that intercepts the requests and the responses.
 * The idea is to log and stamp every request/response by giving a unique
 * id to the request.
 *
 * @author Phann Malinka
 */
package library.filters;

import library.utils.AttributeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
public class LogFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LogFilter.class);

    @Override
    public void init(final FilterConfig filterConfig) {
        logger.info("Initializing filter: {}", this);
    }

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse resp, final FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        /**
         * Generate the request id and put inside the request.
         * This request id will always be put in the response.
         */
        String requestId = UUID.randomUUID().toString();
        request.setAttribute(AttributeUtil.REQUEST_ID, requestId);

        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        // log the request
        logger.info("REQUEST: request id = {}, ip = {}, uri ={}, method = {}",
                requestId,
                ipAddress,
                String.format("%s?%s", request.getRequestURI(), request.getQueryString()),
                request.getMethod()
        );

        chain.doFilter(request, response);

        // and also log the response
        logger.info("RESPONSE: request id = {}, http status = {}",
                requestId,
                response.getStatus()
        );
    }

    @Override
    public void destroy() {
        logger.warn("Destructing filter :{}", this);
    }
}
