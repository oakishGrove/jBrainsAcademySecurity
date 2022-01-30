package account.admin;

import account.admin.dtos.ChangeAccessDto;
import account.admin.dtos.ChangeRoleDto;
import account.admin.dtos.DeleteUserResponseDto;
import account.admin.dtos.UserInfoDto;
import account.dtos.StatusStringDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/admin/user")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PutMapping("/role")
    public UserInfoDto roleOperations(@RequestBody(required = false) ChangeRoleDto changeRoleDto) {
        switch (changeRoleDto.getOperation()) {
            case "GRANT":  return adminService.grantRole(changeRoleDto.getUser(), changeRoleDto.getRole());
            case "REMOVE": return adminService.removeRole(changeRoleDto.getUser(), changeRoleDto.getRole());
            default:       throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid operation specified");
        }
//        return switch (changeRoleDto.getRole()) {
//            case "GRANT" -> adminService.grantRole(changeRoleDto.getUser(), changeRoleDto.getRole());
//            case "REMOVE" -> adminService.removeRole(changeRoleDto.getUser(), changeRoleDto.getRole());
//            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid operation specified");
//        };
    }

    @GetMapping
    public List<UserInfoDto> getUsersInfo() {
        return adminService.getAllUsersInfo();
    }

    @DeleteMapping({"","/{email}" })
    public DeleteUserResponseDto removeUser(@PathVariable(value = "email", required = false) String email) {
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossible");
        }
        adminService.removeUser(email);
        return new DeleteUserResponseDto(email, "Deleted successfully!");
    }

    @PutMapping("/access")
    public StatusStringDto changeUserAccess(@RequestBody ChangeAccessDto changeAccessDto) {
        adminService.changeUserAccess(changeAccessDto);
        return new StatusStringDto(String.format("User %s %s!",
                changeAccessDto.getUser().toLowerCase(Locale.ROOT),
                changeAccessDto.getOperation().equals("LOCK") ?
                        "locked" :
                        "unlocked"
                ));
    }
}
