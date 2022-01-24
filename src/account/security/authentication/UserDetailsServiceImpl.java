package account.security.authentication;

import account.exceptionsmanagament.exceptions.AuthenticationUserDoesntExist;
import account.userdetails.repository.RoleRepository;
import account.userdetails.repository.UserDetailsEntity;
import account.userdetails.repository.UserDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Locale;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {

            var lowerCaseUserName = new String(username).toLowerCase(Locale.ROOT);
//            System.out.println("LOWER CASE USER NAME is: " + lowerCaseUserName );
//            System.out.println(userDetailsRepository.findAll());
//            System.out.println("--# end of user list: \n\n");
            var user = userDetailsRepository.findByEmail(lowerCaseUserName)
                    .orElseThrow(() -> new AuthenticationUserDoesntExist("Nope"));
//            System.out.println("\n\nUSER:" + user);

            var userRoles = roleRepository.findAllByUserDetailsEntity(user);
//            System.out.println("    HIS ROLES:" + userRoles);

            return new UserDetails() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return userRoles;
//                        user.getRoles();
//                        .getRoles().stream()
//                        .map((role) -> (GrantedAuthority)() -> role);
//                return new ArrayList<GrantedAuthority>();
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
                    return true;
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
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
        }
        return null;
    }
}
