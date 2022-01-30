package account.security.securityevents;

import account.security.userdetails.UserEntityService;
import account.security.userdetails.repository.RoleEnum;
import account.security.userdetails.repository.UserDetailsEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserEntityService userEntityService;

    public List<SecurityEntity> getSecurityEvents() {
        return securityEventsDao.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createEventCreateUser(String user) {
        var eventObj = createEventObj(SecurityEvent.CREATE_USER, user);
        securityEventsDao.save(eventObj);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createEventChangePassword(String user) {
        var eventObj = createEventObj(SecurityEvent.CHANGE_PASSWORD, user);
        securityEventsDao.save(eventObj);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createEventAccessDenied(String subject) {
        var eventObj = createEventObj(SecurityEvent.ACCESS_DENIED, subject);
        securityEventsDao.save(eventObj);
    }

    private SecurityEntity createEventObj(SecurityEvent event, String object) {

        System.out.println("CREATING EVENT: "+ event + "with object: " + object);

        var  context = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        var request = context.getRequest();

        var eventObj = new SecurityEntity();
        eventObj.setPath(request.getServletPath());
        eventObj.setDate(new Date());
        eventObj.setObject(object);
        eventObj.setAction(event);


//        var auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null) {
//            System.out.println("AUTH NULLL");
//            SecurityContextHolder.getContext();
//            return eventObj;
//        } else {
//            System.out.println("auth class: " + auth.getClass());
//            if (auth instanceof AnonymousAuthenticationToken) {
//                System.out.println("\n\nif authentication is anonym:" +
//                        auth.getPrincipal());
//            } else {
//                System.out.println("\n\nauthorization else: credentias: " +
//                         auth.getPrincipal());
//                System.out.println(auth.getCredentials());
//            }
//        }


        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            eventObj.setSubject("Anonymous");
        } else {
            var principal = authentication.getPrincipal();
            if (principal == null) {
//                var authToken = SecurityContextHolder.getContext().getAuthentication().getCredentials();
//                System.out.println(authToken);
//                System.out.println(authToken.getClass());
//    //            UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication().getCredentials();
                eventObj.setSubject("Anonymous");
            }

            if (principal instanceof String) {
                if (principal.equals("anonymousUser")) {
                    principal = "Anonymous";
                }

                eventObj.setSubject((String) principal);
            } else if (principal instanceof UserDetailsEntity) {
                eventObj.setSubject( ((UserDetailsEntity) principal).getEmail());
            } else if (principal instanceof UserDetails) {
                eventObj.setSubject(((UserDetails) principal).getUsername());
            }
        }

        return eventObj;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void createEventLoginFailed(String user) {
    public void createEventLoginFailed(String servletPath, String principal) {

        if (principal == null)
            return;

//        var eventObj = createEventObj(SecurityEvent.LOGIN_FAILED, servletPath);
//        eventObj.setSubject(user);

        var  context = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        var request = context.getRequest();

        var eventObj = new SecurityEntity();
        eventObj.setPath(request.getServletPath());
        eventObj.setDate(new Date());
        eventObj.setObject(servletPath);
        eventObj.setAction(SecurityEvent.LOGIN_FAILED);
        eventObj.setSubject(principal);
//        var  context = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        var request = context.getRequest();
//        eventObj.setPath(request.getServletPath());
        securityEventsDao.save(eventObj);


        var optionalUserEntity = userEntityService.findUserDetailsEntityByEmail(principal);

        if (optionalUserEntity.isPresent()) {
            var userEntity = optionalUserEntity.get();
            if (userEntity.getFailedAttempts() + 1 > 5) {
                userEntity.setLocked(true);
                userEntity.setFailedAttempts(6);

                createEventBruteForce(servletPath, principal);
                createEventLockUser(principal, principal);
            } else {
                userEntity.setFailedAttempts(userEntity.getFailedAttempts() + 1);
            }

            userEntityService.updateUserDetailsEntity(userEntity);
//            var brute = new SecurityEntity();
//            brute.setPath(request.getServletPath());
//            brute.setDate(new Date());
//            brute.setSubject(principal);
//            brute.setAction(SecurityEvent.BRUTE_FORCE);
//            brute.setObject(servletPath);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createEventGrantRole(RoleEnum role, String email) {
        var eventObj = createEventObj(
                SecurityEvent.GRANT_ROLE,
                String.format("Grant role %s to %s", role.name(), email));
        securityEventsDao.save(eventObj);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createEventRemoveRole(RoleEnum role, String email) {
        var eventObj = createEventObj(
                SecurityEvent.REMOVE_ROLE,
                String.format("Remove role %s from %s", role.name(), email));
        securityEventsDao.save(eventObj);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createEventDeleteUser(String email) {
        var eventObj = createEventObj(SecurityEvent.DELETE_USER, email);
        securityEventsDao.save(eventObj);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createEventLockUser(String email, String principal) {
        var eventObj = createEventObj(
                SecurityEvent.LOCK_USER,
                String.format("Lock user %s", email));
        eventObj.setSubject(principal);
        securityEventsDao.save(eventObj);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createEventUnLockUser(String email) {
        var eventObj = createEventObj(
                SecurityEvent.UNLOCK_USER,
                String.format("Unlock user %s", email));
        securityEventsDao.save(eventObj);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createEventBruteForce(String path, String principal) {
        var eventObj = createEventObj(SecurityEvent.BRUTE_FORCE, path);
        eventObj.setSubject(principal);
        securityEventsDao.save(eventObj);
    }
}
