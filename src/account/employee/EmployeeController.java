package account.employee;

import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
//@Validated
public class EmployeeController {

    private final EmployeeService service;

    @GetMapping("api/empl/payment")
    @Secured(value = {"ROLE_USER", "ROLE_ACCOUNTANT"})
    public Object getPayroll(@AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam(value = "period", required = false) String periodString) {
        return service.getUserPayroll(userDetails, periodString);
    }

}
