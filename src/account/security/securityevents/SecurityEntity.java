package account.security.securityevents;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "security_events")
@Data
public class SecurityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date date;
    @Enumerated(EnumType.STRING)
    private SecurityEvent action;
    private String subject; //user email or Anonymous
    private String object;
    private String path;
}
