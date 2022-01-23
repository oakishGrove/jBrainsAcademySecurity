package account.accountant.dao;

import account.dtos.PayRollDto;

import java.time.LocalDate;
import java.util.List;

public interface AccountantDao {
    void uploadPayrolls(List<PayRollDto> paymentDtoList);

    void updatePayroll(PayRollDto paymentDto);

    PayRollEntity getPayroll(Long userId, LocalDate period);

    List<PayRollEntity> getPayrolls(Long userId);
}
