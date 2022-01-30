package account.security.userdetails.repository;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
    @ManyToMany(fetch = FetchType.LAZY)
//    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<UserDetailsEntity> users;

    public String getAuthority() {
        return "ROLE_" + role.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Role role1 = (Role) o;

        return this.role.equals(role1.role);
    }


    public boolean equals(RoleEnum o) {
        return this.role.equals(o);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role=" + role +
                '}';
    }

    @Override
    public int hashCode() {
        return 31 * id.hashCode() + 31 * role.hashCode();
    }
}