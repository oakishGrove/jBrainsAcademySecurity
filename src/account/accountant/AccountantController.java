package account.accountant;

import account.dtos.PayRollDto;
import account.dtos.StatusStringDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/acct")
@AllArgsConstructor
public class AccountantController {
    private final AccountantService accountantService;

    @PostMapping("/payments")
    public StatusStringDto uploadPayrolls(@RequestBody @Valid List<PayRollDto> paymentDtoList) {
        return accountantService.uploadPayrolls(paymentDtoList);
    }

    @PutMapping("/payments")
    public StatusStringDto changeUserSalary(@RequestBody @Valid PayRollDto paymentDto) {
        return accountantService.changeUserSalary(paymentDto);
    }
}
