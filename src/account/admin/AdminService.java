package account.admin;

import account.admin.dtos.ChangeAccessDto;
import account.admin.dtos.UserInfoDto;
import account.security.userdetails.UserEntityService;
import account.security.userdetails.repository.Role;
import account.security.userdetails.repository.RoleEnum;
import account.security.userdetails.repository.RoleRepository;
import account.security.userdetails.repository.UserDetailsEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminService {

    private final UserEntityService userEntityService;
    private final RoleRepository roleRepository;

    @Transactional
    public UserInfoDto grantRole(String user, String roleString) {

        var role = createRole(roleString);

        var userDetails = userEntityService
                .findUserDetailsEntityByEmail(user)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found!"));

        if (userDetails.getRoles().contains(role)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User all ready has this role");
        }

        if (isViolatedRolesConstraints(role, userDetails.getRoles())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The user cannot combine administrative and business roles!");
        }

        var dbRole = roleRepository.findByRole(role.getRole());
        if (dbRole == null) throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "This role doesnt exist");


        userDetails.getRoles().add(dbRole);
        var updatedUser = userEntityService.updateUserDetailsEntity(userDetails);

        return mapToUserInfoDto(updatedUser);
    }


    private boolean isViolatedRolesConstraints(Role role, List<Role> userRoles) {
        List<RoleEnum> administriveRoles = List.of(RoleEnum.ADMINISTRATOR);
        List<RoleEnum> businessRoles = List.of(RoleEnum.ACCOUNTANT, RoleEnum.USER, RoleEnum.AUDITOR);

        if (administriveRoles.contains(role.getRole())) {
            for (var userRole : userRoles) {
                if (businessRoles.contains(userRole.getRole())) {
                    return true;
                }
            }
        }
        if (businessRoles.contains(role.getRole())) {
            for (var userRole : userRoles) {
                if (administriveRoles.contains(userRole.getRole())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Transactional
    public UserInfoDto removeRole(String user, String roleString) {
        var role = createRole(roleString);
        if (role.equals(RoleEnum.ADMINISTRATOR)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Can't remove ADMINISTRATOR role!");
        }

        var userDetails = userEntityService
                .findUserDetailsEntityByEmail(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        var removed = userDetails
                .getRoles()
                .removeIf(r -> r.equals(role));

        if (!removed) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The user does not have a role!");
        } else if (userDetails.getRoles().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The user must have at least one role!");
        } else {
            var updateUserDetails = userEntityService.updateUserDetailsEntity(userDetails);
            return mapToUserInfoDto(updateUserDetails);
        }
    }

    @Transactional
    public List<UserInfoDto> getAllUsersInfo() {
        return  userEntityService
                .findAll()
                .stream().map(this::mapToUserInfoDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public void removeUser(String email) {

        var user = userEntityService
                .findUserDetailsEntityByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found!"));

        if (user.getRoles().contains(RoleEnum.ADMINISTRATOR)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Can't remove ADMINISTRATOR role!");
        }

        for (var role : user.getRoles()) {
            if (role.equals(RoleEnum.ADMINISTRATOR)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Can't remove ADMINISTRATOR role!");
            }
        }

        userEntityService.removeUser(email);
    }

    private UserInfoDto mapToUserInfoDto(UserDetailsEntity userDetailsEntity) {

        UserInfoDto userInfoDto = UserInfoDto.builder()
                .id(userDetailsEntity.getId())
                .name(userDetailsEntity.getName())
                .lastname(userDetailsEntity.getLastname())
                .email(userDetailsEntity.getEmail())
                .roles(userDetailsEntity.getRoles()
                        .stream()
                        .map(Role::getAuthority)
                        .collect(Collectors.toList()))
                .build();
        Collections.sort(userInfoDto.getRoles());
        return userInfoDto;
    }

    private Role createRole(String roleString) {
        try {
            var roleObj = new Role();
            roleObj.setRole(RoleEnum.valueOf(roleString));
            return roleObj;
        } catch (Exception ex) { }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!");
    }

    public void changeUserAccess(ChangeAccessDto changeAccessDto) {
        var user = userEntityService
                .findUserDetailsEntityByEmail(changeAccessDto.getUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "This user doesnt exist"));

        if (user.getRoles().contains(RoleEnum.ADMINISTRATOR)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Can't lock the ADMINISTRATOR");
        }

        switch (changeAccessDto.getOperation()) {
            case "LOCK": user.setLocked(true); break;
            case "UNLOCK" : user.setLocked(false); break;
            default: throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Undefined operation specified");
        }
        userEntityService.updateUserDetailsEntity(user);
    }
}
