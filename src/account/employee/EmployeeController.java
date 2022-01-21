package account.employee;

import account.dtos.UserDto;
import account.security.userdetails.repository.UserDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService service;
    private final UserDetailsRepository repository;

    @GetMapping("/empl/payment")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getPayrolls(@AuthenticationPrincipal UserDetails userDetails, Authentication auth) {
        try {
            System.out.println("\n\nGetPayRolls Is called AuthenticationPrincipal:");
            System.out.println(userDetails.getUsername());
            System.out.println(userDetails.getPassword());
        } catch (Exception ex) {}

//        try {
//            System.out.println("\n\nauth");
//            System.out.println((UsernamePasswordAuthenticationToken) auth.getCredentials());
//            System.out.println(auth.getDetails());
//            var det = (UserDetails) auth.getPrincipal();
//            System.out.println(det.getUsername());
//            System.out.println(det.getPassword());
//        } catch (Exception ex) {}

        return service.getUser(userDetails.getUsername());
    }

}
