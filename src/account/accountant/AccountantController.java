package account.accountant;

import account.dtos.PayRollDto;
import account.dtos.StatusStringDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/acct")
@AllArgsConstructor
@Validated // so that it would scan List<>
public class AccountantController {
    private final AccountantService accountantService;

    @PostMapping("/payments")
    @Secured({"ROLE_ACCOUNTANT"})
    public StatusStringDto uploadPayrolls(@RequestBody(required = false) @Valid List<PayRollDto> paymentDtoList) {
        if (paymentDtoList == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body required");
        }
        return accountantService.uploadPayrolls(paymentDtoList);
    }

    @PutMapping("/payments")
    @Secured({"ROLE_ACCOUNTANT"})
    public StatusStringDto changeUserSalary(@RequestBody(required = false) @Valid PayRollDto paymentDto) {
        if (paymentDto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body required");
        }
        return accountantService.changeUserSalary(paymentDto);
    }
}
