package account.security.userdetails.repository;

import account.security.securityevents.jpalisteners.UserDetailsEntityListener;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@EntityListeners(UserDetailsEntityListener.class)
public class UserDetailsEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true)
    private String email;
    private String name;
    private String lastname;
    private String password;
    private Boolean locked;
    @ManyToMany(fetch = FetchType.EAGER)
//    @ManyToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = {
                    @JoinColumn(
                            name = "user_id",
                            referencedColumnName = "id",
                            nullable = false,
                            updatable = false
                    ),
                    @JoinColumn(
                            name = "role_id",
                            referencedColumnName = "id",
                            nullable = false,
                            updatable = false
                    )
            }
    )
    private List<Role> roles;

    public static UserDetailsEntity createInstance(String email, String name, String lastname, String encodedPassword) {
        var userEntity = new UserDetailsEntity();
        userEntity.setEmail(email);
        userEntity.setName(name);
        userEntity.setLastname(lastname);
        userEntity.setPassword(encodedPassword);
        userEntity.setLocked(false);
        return userEntity;
    }
}
