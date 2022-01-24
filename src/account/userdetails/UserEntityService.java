package account.userdetails;

import account.userdetails.repository.UserDetailsEntity;
import account.userdetails.repository.UserDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserEntityService {

    private final UserDetailsRepository userDetailsRepository;

    @Transactional
    public UserDetailsEntity updateUserDetailsEntity(UserDetailsEntity userDetailsEntity) {
        return userDetailsRepository.save(userDetailsEntity);
    }

    public Optional<UserDetailsEntity> findUserDetailsEntityByEmail(String email) {
        return userDetailsRepository.findByEmail(new String(email).toLowerCase(Locale.ROOT));
    }

    public List<UserDetailsEntity> findAll() {
        return userDetailsRepository.findAll();
    }

    public void removeUser(String email) {
        var userToBeRemoved = userDetailsRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        userDetailsRepository.delete(userToBeRemoved);
    }
}
