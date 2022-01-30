package account.security.securityevents.jpalisteners;

import account.security.securityevents.SecurityEventsService;
import account.security.userdetails.repository.UserDetailsEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

@Component
public class UserDetailsEntityListener {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsEntityListener.class);
    private SecurityEventsService securityEventsService;

    @Autowired
    public UserDetailsEntityListener(SecurityEventsService securityEventsService) {
        this.securityEventsService = securityEventsService;
    }

    public UserDetailsEntityListener() {}

    @PostPersist
    private void afterPersistingUser(UserDetailsEntity entity) {
        log.debug("A user has been successfully registered id:"+entity.getId());
        securityEventsService.createEventCreateUser(entity.getEmail());
    }

//    @PostUpdate
//    public void afterUpdate(UserDetailsEntity entity) {
//    }

}
