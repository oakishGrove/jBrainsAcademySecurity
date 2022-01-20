package account.security.userdetails.repository;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true)
    private String email;
    private String name;
    private String lastname;
    private String password;
//    @OneToMany
//    private List<Role> roles;
}
