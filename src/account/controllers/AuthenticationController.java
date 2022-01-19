package account.controllers;

import account.dtos.ErrorDto;
import account.dtos.SignUpDto;
import account.dtos.SignUpResponseDto;
import account.service.AccountsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("api/auth")
@AllArgsConstructor
public class AuthenticationController {
    
    private final AccountsService accountsService;

    @PostMapping("/signup")
    SignUpResponseDto singUp(@RequestBody SignUpDto signUpDto) {
        return accountsService.signUp(signUpDto);
    }

    @PostMapping("/changepass")
    void changePass() {

    }

}
