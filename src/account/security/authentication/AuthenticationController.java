package account.security.authentication;

import account.dtos.ChangePasswordDto;
import account.dtos.ChangePasswordResponseDto;
import account.dtos.SignUpDto;
import account.dtos.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipal;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {
    
    private final AuthenticationService accountsService;

    @PostMapping("/signup")
    public UserDto singUp(@Valid @RequestBody SignUpDto signUpDto) {
        return accountsService.signUp(signUpDto);
    }

    @PostMapping("/changepass")
    public ChangePasswordResponseDto changePass(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid ChangePasswordDto dto) {
        return accountsService.changePassword(dto, userDetails);
    }

}
