package account.security.authentication;

import account.dtos.SignUpDto;
import account.dtos.UserDto;
import account.exceptions.SignUpValidationException;
import account.exceptions.UserAlreadyExistException;
import account.security.userdetails.repository.UserDetails;
import account.security.userdetails.repository.UserDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsRepository repository;

    public UserDto signUp(SignUpDto dto) {
        if (!validateSignUp(dto)) {
            throw new SignUpValidationException("/api/auth/signup");
        }

        if (repository.existsByEmail(dto.getEmail().toLowerCase(Locale.ROOT))) {
            throw new UserAlreadyExistException("User exist!");
        }

        var userEntity = new UserDetails();
        userEntity.setName(dto.getName());
        userEntity.setLastname(dto.getLastname());
        userEntity.setEmail(new String(dto.getEmail().toLowerCase(Locale.ROOT)));
        userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));

        var savedEntity = repository.save(userEntity);

        return UserDto.builder()
                .email(dto.getEmail())
                .lastname(dto.getLastname())
                .name(dto.getName())
                .id(savedEntity.getId())
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
