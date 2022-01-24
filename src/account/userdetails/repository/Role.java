package account.userdetails.repository;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Data
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String authority;
    @ManyToOne
//    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserDetailsEntity userDetailsEntity;

    public static Role buildInstance(String authority, UserDetailsEntity user) {
        var role = new Role();
        role.setUserDetailsEntity(user);
        role.setAuthority(authority);
        return role;
    }
}
