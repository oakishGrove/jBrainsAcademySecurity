package account.security.securityevents;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventLogginFactory {
    private final SecurityEventsDao dao;
    private static EventLoggin logger = null;

    public EventLoggin getInstance() {
        if (logger == null) {
            logger = new EventLoggin(dao);
        }
        return logger;
    }
}
