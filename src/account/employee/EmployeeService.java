package account.employee;

import account.accountant.dao.AccountantDao;
import account.dtos.PayRollResponseDto;
import account.security.userdetails.repository.UserDetailsRepository;
import account.utils.DateConverterUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final UserDetailsRepository repository;
    private final AccountantDao accountantDao;


    public Object getUserPayroll(UserDetails userDetails, String periodString) {

        var userDetailsEntity = repository.findByEmail(userDetails.getUsername()).get();
        System.out.println("" + userDetailsEntity);

        LocalDate period = DateConverterUtil.fromString(periodString);
        if (period == null) {
            return accountantDao
                    .getPayrolls(userDetailsEntity.getId())
                    .stream()
                    .map(entity ->
                            PayRollResponseDto.builder()
                                    .name(userDetailsEntity.getName())
                                    .lastname(userDetailsEntity.getLastname())
                                    .salary(String.format("%d dollar(s) %d cent(s)", entity.getSalary() / 100, entity.getSalary() % 100))
                                    .period(entity.getPeriod())
                                    .build())
                    .collect(Collectors.toList());
        } else {

            var payRollEntity = accountantDao.getPayroll(
                    userDetailsEntity.getId(),
                    period);

            return PayRollResponseDto.builder()
                        .name(userDetailsEntity.getName())
                        .lastname(userDetailsEntity.getLastname())
                        .salary(String.format("%d dollar(s) %d cent(s)", payRollEntity.getSalary() / 100, payRollEntity.getSalary() % 100))
                        .period(payRollEntity.getPeriod())
                        .build();
        }
    }
}
