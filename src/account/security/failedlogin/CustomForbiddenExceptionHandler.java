package account.security.failedlogin;

import account.security.securityevents.SecurityEventsService;
import account.security.userdetails.repository.UserDetailsEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class CustomForbiddenExceptionHandler implements AccessDeniedHandler {
    private final SecurityEventsService securityEventsService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Authentication auth
                = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth);
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied!");
//        response.setStatus(403);
//        eventsService.createEventAccessDenied(request.getServletPath());


        securityEventsService.createEventAccessDenied(request.getServletPath());
//        securityEventsService.createEventAccessDenied(((UserDetails)auth.getPrincipal()).getUsername());
    }
}
