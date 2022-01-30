package account.security.failedlogin;

import account.security.securityevents.SecurityEventsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final SecurityEventsService securityEventsService;
    private final BasicAuthenticationConverter basicAuthenticationConverter;
    private ObjectMapper objectMapper = new ObjectMapper();

    public RestAuthenticationEntryPoint(SecurityEventsService securityEventsService, BasicAuthenticationConverter basicAuthenticationConverter) {
        this.securityEventsService = securityEventsService;
        this.basicAuthenticationConverter = basicAuthenticationConverter;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        var token = basicAuthenticationConverter.convert(request);
//        var token = basicAuthenticationConverter.convert(request);
        System.out.println(request.getContextPath());
        System.out.println(request.getServletPath());
        String principal = token == null ? "Anonymous" : token.getPrincipal().toString();
        System.out.println(principal);
        response.setStatus(401);
        if (authException instanceof LockedException) {
            Map<String, Object> data = new HashMap<>();
            data.put("timestamp", new Date().toString());
            data.put("message", "User account is locked");
            data.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            data.put("path", request.getServletPath());
            data.put("status", Integer.parseInt("401"));

            response.getOutputStream()
                    .println(objectMapper.writeValueAsString(data));
            return;
        }

        if (token == null)
            return;

        securityEventsService.createEventLoginFailed(request.getServletPath(), token.getPrincipal().toString());
    }
}
