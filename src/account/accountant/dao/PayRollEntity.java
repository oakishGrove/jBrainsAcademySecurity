package account.accountant.dao;

import account.security.userdetails.repository.UserDetailsEntity;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "payroll")
public class PayRollEntity {
    @Id
    @SequenceGenerator(name="payroll_sq_name", initialValue = 2, allocationSize = 1,
    sequenceName = "payroll_sq")
    @GeneratedValue(generator = "payroll_sq_name", strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long salary;
    private LocalDate period;
//    @ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false, updatable = false)
    private UserDetailsEntity employee;
}
