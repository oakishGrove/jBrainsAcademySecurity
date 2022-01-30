package account.security.failedlogin;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogingErrorLoggerFilter extends GenericFilterBean {
//public class LoggingErrorFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try{
            Authentication auth = SecurityContextHolder.getContext()
                    .getAuthentication();

            System.out.println("##---FILTER IN ACTION BEFORE FILTER---##");
            var principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                System.out.println(((UserDetails)principal).getUsername());
            } else {
                System.out.println(principal);
            }
            System.out.println(request.getServletContext().getContextPath());
            System.out.println(((HttpServletRequest) request).getServletPath());
            System.out.println(((HttpServletRequest) request).getContextPath());
            System.out.println(((HttpServletResponse) response).getStatus());
            System.out.println("##---FILTER IN ACTION BEFORE FILTER---##");
        } catch (Exception ex) {}

        chain.doFilter(request, response);

        try {

            var auth = SecurityContextHolder.getContext()
                    .getAuthentication();

            System.out.println("##---FILTER IN ACTION AFTER FILTER---##");
            var principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                System.out.println(((UserDetails)principal).getUsername());
            } else {
                System.out.println(principal);
            }
            System.out.println(request.getServletContext().getContextPath());
            System.out.println(((HttpServletRequest) request).getServletPath());
            System.out.println(((HttpServletRequest) request).getContextPath());
            System.out.println(((HttpServletResponse) response).getStatus());
            System.out.println("##---FILTER IN ACTION AFTER FILTER---##");
        } catch (Exception ex) {}
    }

}
