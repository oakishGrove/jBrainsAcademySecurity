package account.security.securityevents;

import account.security.userdetails.repository.UserDetailsEntity;
import org.h2.engine.User;

import java.time.LocalDate;
import java.util.Date;

public class EventLoggin {
    private final SecurityEventsDao dao;

    public EventLoggin(SecurityEventsDao dao_) {
        this.dao = dao_;
    }

    public void logUserCreated(UserDetailsEntity subject, UserDetailsEntity object) {
        var evet = new SecurityEntity();
        evet.setDate(new Date());
        evet.setAction(SecurityEvent.CREATE_USER);
        evet.setSubject(subject.getEmail());
        evet.setObject(object.getEmail());
        evet.setPath("");
    }

}
