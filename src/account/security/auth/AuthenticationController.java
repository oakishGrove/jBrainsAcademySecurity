package account.security.auth;

import account.dtos.ChangePasswordDto;
import account.dtos.ChangePasswordResponseDto;
import account.dtos.SignUpDto;
import account.dtos.UserDto;
import account.security.securityevents.SecurityEventsDao;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {
    
    private final AuthenticationService accountsService;
    private final SecurityEventsDao dao;

    @PostMapping("/signup")
    public UserDto singUp(@Valid @RequestBody SignUpDto signUpDto) {
        UserDto userDto = accountsService.signUp(signUpDto);
        System.out.println("Exiting: security styff : " + dao.findAll());
        return userDto;
    }

    @PostMapping("/changepass")
    public ChangePasswordResponseDto changePass(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid ChangePasswordDto dto) {
        return accountsService.changePassword(dto, userDetails);
    }

}
