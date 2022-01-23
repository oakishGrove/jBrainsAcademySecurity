package account.userdetails.repository;

import lombok.Data;

import javax.persistence.*;

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
//    @OneToMany
//    private List<Role> roles;
}
