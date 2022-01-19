package account.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/user")
public class ServiceController {

    @PutMapping("/role")
    void changeUserRole() {}

    @DeleteMapping
    void deleteUser() {}

    @GetMapping
    void getAllUsers() {}
}
