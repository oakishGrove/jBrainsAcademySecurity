package account.security.auth;

import account.dtos.ChangePasswordDto;
import account.dtos.ChangePasswordResponseDto;
import account.dtos.SignUpDto;
import account.dtos.UserDto;
import account.exceptionsmanagament.exceptions.SignUpValidationException;
import account.exceptionsmanagament.exceptions.UserAlreadyExistException;
import account.security.securityevents.SecurityEventsService;
import account.security.userdetails.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsRepository repository;
    private final RoleRepository roleRepository;
    private final SecurityEventsService securityEventsService;
    private static List<String> breachedPasswords = List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch",
            "PasswordForApril", "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");

    @Transactional
    public UserDto signUp(SignUpDto dto) {

        System.out.println("\n\nREGISTERING DTO: " + dto);

        if (!validateSignUp(dto)) {
            throw new SignUpValidationException("/api/auth/signup");
        }

        if (checkBreachedPasswords(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        }

        if (repository.existsByEmail(dto.getEmail().toLowerCase(Locale.ROOT))) {
            throw new UserAlreadyExistException("User exist!");
        }

        var userEntity = UserDetailsEntity.createInstance(
                new String(dto.getEmail()).toLowerCase(Locale.ROOT),
                dto.getName(),
                dto.getLastname(),
                passwordEncoder.encode(dto.getPassword()));
//        var savedUserEntity = repository.save(userEntity);

        var roleEnum = RoleEnum.USER;
        if (repository.findAll(
                PageRequest.of(0, 1))
                .getTotalElements() == 0) {
            roleEnum = RoleEnum.ADMINISTRATOR;
        }

        var role = roleRepository.findByRole(roleEnum);
        userEntity.setRoles(List.of(role));

        var savedUserEntity = repository.save(userEntity);

        System.out.println("Exiting: all saved: " + repository.findAll());

        return UserDto.builder()
                .email(dto.getEmail())
                .lastname(dto.getLastname())
                .name(dto.getName())
                .id(savedUserEntity.getId())
                .roles(List.of(role.getAuthority()))
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

    public ChangePasswordResponseDto changePassword(
            ChangePasswordDto dto,
            org.springframework.security.core.userdetails.UserDetails userDetails) {

        if (checkBreachedPasswords(dto.getNew_password())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        }

        if (passwordEncoder.matches(dto.getNew_password(), userDetails.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The passwords must be different!");
        }

        var changedUser = repository
                .findByEmail(userDetails.getUsername())
                .get();
        changedUser
                .setPassword(passwordEncoder.encode(dto.getNew_password()));

        var updatedUser = repository.save(changedUser);

        securityEventsService.createEventChangePassword(updatedUser.getEmail());

        return ChangePasswordResponseDto.builder()
                .email(userDetails.getUsername())
                .status("The password has been updated successfully")
                .build();
    }

    private boolean checkBreachedPasswords(String rawPassword) {
        return breachedPasswords.stream()
                .anyMatch((String pw) ->  pw.equals(rawPassword));
    }
}
