package account.employee;

import account.dtos.UserDto;
import account.security.userdetails.UserMapper;
import account.security.userdetails.repository.UserDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final UserDetailsRepository repository;

    public UserDto getUser(String email) {
        return UserMapper.mapToUserDto(
                repository.findByEmail(email)
                        .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED)));
    }
}
