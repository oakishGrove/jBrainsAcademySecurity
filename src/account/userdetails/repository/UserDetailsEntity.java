package account.userdetails.repository;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Locale;

@Data
@Entity
@Table(name = "user_details")
public class UserDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true)
    private String email;
    private String name;
    private String lastname;
    private String password;
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Role> roles;

    public static UserDetailsEntity createInstance(String email, String name, String lastname, String encodedPassword) {
        var userEntity = new UserDetailsEntity();
        userEntity.setEmail(email);
        userEntity.setName(name);
        userEntity.setLastname(lastname);
        userEntity.setPassword(encodedPassword);
        return userEntity;
    }
}
