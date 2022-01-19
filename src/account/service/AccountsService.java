package account.service;

import account.dtos.SignUpDto;
import account.dtos.SignUpResponseDto;
import account.exceptions.SignUpValidationException;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {

    public SignUpResponseDto signUp(SignUpDto dto) {
        if (!validateSignUp(dto)) {
            throw new SignUpValidationException("/api/auth/signup");
        }

        return SignUpResponseDto.builder()
                .email(dto.getEmail())
                .lastname(dto.getLastname())
                .name(dto.getName())
                .build();
    }

    private boolean validateSignUp(SignUpDto signUpDto) {
        return signUpDto != null
                && signUpDto.getEmail() != null
                && signUpDto.getEmail().endsWith("@acme.com")
                && signUpDto.getName() != null
                && !signUpDto.getName().isEmpty()
                && signUpDto.getLastname() != null
                && !signUpDto.getLastname().isEmpty()
                && signUpDto.getPassword() != null
                && !signUpDto.getPassword().isEmpty();
    }
}
