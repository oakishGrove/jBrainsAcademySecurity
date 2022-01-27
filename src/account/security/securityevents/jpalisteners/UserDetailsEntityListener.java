package account.security.securityevents.jpalisteners;

import account.security.securityevents.SecurityEventsDao;
import account.security.securityevents.SecurityEventsService;
import account.security.userdetails.repository.UserDetailsEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Component
public class UserDetailsEntityListener {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsEntityListener.class);
//    @Autowired
    private SecurityEventsService securityEventsService;

    @Autowired
    public UserDetailsEntityListener(SecurityEventsService securityEventsService) {
        this.securityEventsService = securityEventsService;
    }

    public UserDetailsEntityListener() {}

    @PostPersist
    @PrePersist
    @PreUpdate
    private void afterPersistingUser(UserDetailsEntity entity) {

        log.debug("A user has been successfully registered id:"+entity.getId());
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            securityEventsService.createEventCreateUser(entity, userDetails.getUsername());
        }
        if (principal instanceof String) {
            if (principal.equals("anonymousUser")) {
              principal = "Anonymous";
            }
            securityEventsService.createEventCreateUser(entity, (String) principal);
        }

        var principal1 = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(principal1);
        System.out.println(principal1.getClass());
//        var userDetails = (UsernamePasswordAuthenticationToken) principal1;
//        System.out.println(userDetails);
    }



}
