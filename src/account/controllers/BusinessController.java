package account.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BusinessController {

    @GetMapping("/empl/payment")
    void getPayrolls() {

    }

    @PostMapping("/acct/payments")
    void updatePayrolls() {}

    @PutMapping("/acct/payments")
    void updatePaymentInformation() {}
}
