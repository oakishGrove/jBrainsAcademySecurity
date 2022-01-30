package account.security.failedlogin;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class FailedAuth {


    @EventListener
    public void onFailure(AuditApplicationEvent event) {
        AuditEvent auditEvent = event.getAuditEvent();
        System.out.println("\n**\nEVENT\n**\n"+auditEvent.getType());

    }

}
//public class FailedAuth implements AuthenticationFailureHandler {
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        if
//
//    }
//}
