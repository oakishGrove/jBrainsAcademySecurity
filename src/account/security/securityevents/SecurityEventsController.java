package account.security.securityevents;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/security/events")
@AllArgsConstructor
public class SecurityEventsController {

    private final SecurityEventsService securityEventsService;

    @GetMapping
    public List<SecurityEntity> getSecurityEvents() {
        return securityEventsService.getSecurityEvents();
    }

}
