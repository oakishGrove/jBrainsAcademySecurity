package account.security.authentication;

import account.dtos.SignUpDto;
import account.dtos.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@AllArgsConstructor
public class AuthenticationController {
    
    private final AuthenticationService accountsService;

    @PostMapping("/signup")
    UserDto singUp(@RequestBody SignUpDto signUpDto) {
        return accountsService.signUp(signUpDto);
    }

    @PostMapping("/changepass")
    void changePass() {

    }

}
