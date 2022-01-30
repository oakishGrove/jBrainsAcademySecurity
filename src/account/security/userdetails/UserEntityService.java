package account.security.userdetails;

import account.exceptionsmanagament.exceptions.AuthenticationUserDoesntExist;
import account.security.userdetails.repository.Role;
import account.security.userdetails.repository.UserDetailsEntity;
import account.security.userdetails.repository.UserDetailsRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@AllArgsConstructor
//public class UserEntityService {
public class UserEntityService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger("CustomUserEntityService");

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

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userDetailsRepository
                .findByEmail(new String(username).toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new AuthenticationUserDoesntExist("Nope"));
        System.out.println("**fetching user for authentication(load by user): " + user.getEmail());
        log.info("fetching user for authentication(load by user): " + user.getEmail());
        var rolesList = new ArrayList<Role>(user.getRoles());

        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return rolesList;
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getEmail();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return !user.getLocked();
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }
}
