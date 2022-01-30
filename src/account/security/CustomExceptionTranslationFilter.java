package account.security;

import account.security.securityevents.SecurityEventsService;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomExceptionTranslationFilter extends ExceptionTranslationFilter {
    private SecurityEventsService securityEventsService;

    public CustomExceptionTranslationFilter(AuthenticationEntryPoint authenticationEntryPoint,
                                            SecurityEventsService securityEventsService) {
        this(authenticationEntryPoint, new HttpSessionRequestCache(), securityEventsService);
    }

    public CustomExceptionTranslationFilter(AuthenticationEntryPoint authenticationEntryPoint,
                                            RequestCache requestCache,
                                            SecurityEventsService securityEventsService) {
        super(authenticationEntryPoint, requestCache);
        this.securityEventsService = securityEventsService;
    }

    // TODO: why this one doesn't catch exceptions from BasicAuthenticationFilter it seems it tunnes
    //  exceptions to authentication entry point it self(BasicAuth)
    // Authentication exception is cathed by both
    @Override
    protected void sendStartAuthentication(HttpServletRequest request,
                                           HttpServletResponse response, FilterChain chain,
                                           AuthenticationException reason) throws ServletException, IOException {
        System.out.println("**************************");
        System.out.println("custom send Start authentication");
        System.out.println("**************************");
        var auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth);
        System.out.println("This is credentials :" + auth.getCredentials());
        System.out.println("This is principal: " + auth.getPrincipal());

        String principal = null;
        if (auth.getPrincipal() instanceof UserDetails) {
            principal = ((UserDetails) auth.getPrincipal()).getUsername();
        } else if (auth.getPrincipal() instanceof String
                && !auth.getPrincipal().equals("anonymousUser")) {
            principal = (String) auth.getPrincipal();
        }
//        System.out.println(auth.getCredentials());
//        System.out.println("-- PATH STUFF --");
//        System.out.println(request.getServletPath());

        securityEventsService.createEventLoginFailed(request.getServletPath(), principal);
//        securityEventsService.createEventLoginFailed(request.getServletPath());
        super.sendStartAuthentication(request, response, chain, reason);
    }
}