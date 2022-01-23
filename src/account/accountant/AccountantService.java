package account.accountant;

import account.accountant.dao.AccountantDao;
import account.dtos.PayRollDto;
import account.dtos.StatusStringDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountantService {

    private final AccountantDao accountantDao;

    public StatusStringDto uploadPayrolls(List<PayRollDto> paymentDtoList) {
        accountantDao.uploadPayrolls(paymentDtoList);
        return new StatusStringDto("Added successfully!");
    }

    public StatusStringDto changeUserSalary(PayRollDto paymentDto) {
        accountantDao.updatePayroll(paymentDto);
        return new StatusStringDto("Updated successfully!");
    }
}
