package account.security.securityevents;

import account.security.userdetails.repository.UserDetailsEntity;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class SecurityEventsService {

    private final SecurityEventsDao securityEventsDao;

    public List<SecurityEntity> getSecurityEvents() {
        return securityEventsDao.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createEventCreateUser(UserDetailsEntity entity, String user) {
        System.out.println("CREATE VEVENT CREATED USER:+" + user);
        var  context = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        var request = context.getRequest();
        var eventObj = new SecurityEntity();
        eventObj.setPath(request.getServletPath());
        eventObj.setAction(SecurityEvent.CREATE_USER);
        eventObj.setDate(new Date());
        eventObj.setSubject(user);
        eventObj.setObject(entity.getEmail());
        securityEventsDao.save(eventObj);
    }
}
