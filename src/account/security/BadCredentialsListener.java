//package account.security;
//
//import account.security.securityevents.SecurityEventsService;
//import lombok.AllArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.ApplicationListener;
//import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
//import org.springframework.stereotype.Component;
//
//@Component
//@AllArgsConstructor
//public class BadCredentialsListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
//    private static final Logger LOG = LoggerFactory.getLogger(BadCredentialsListener.class);
//    private final SecurityEventsService securityEventsService;
//
//    @Override
//        public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
//            Object userName = event.getAuthentication().getPrincipal();
//            Object credentials = event.getAuthentication().getCredentials();
//            securityEventsService.createEventLoginFailed((String) userName);
//            LOG.debug("Failed login using USERNAME [" + userName + "]");
//            LOG.debug("Failed login using PASSWORD [" + credentials + "]");
//        }
//}
