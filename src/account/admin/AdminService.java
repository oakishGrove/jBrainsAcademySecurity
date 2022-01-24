package account.admin;

import account.admin.dtos.UserInfoDto;
import account.dtos.UserDto;
import account.userdetails.UserEntityService;
import account.userdetails.UserMapper;
import account.userdetails.repository.Role;
import account.userdetails.repository.RoleRepository;
import account.userdetails.repository.UserDetailsEntity;
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

    // TODO: no dublicate roles, and role types
    @Transactional
    public UserInfoDto grantRole(String user, String role) {
        checkRoleName(role);
        var userDetails = userEntityService
                .findUserDetailsEntityByEmail(user)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found!"));

        var userRoles = roleRepository
                .findAllByUserDetailsEntity(userDetails).stream()
                .map(Role::getAuthority)
                .collect(Collectors.toList());

        if (userRoles.contains(role)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User all ready has this role");
        }

        if (isViolatedRolesConstraints(role, userRoles)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The user cannot combine administrative and business roles!");
        }

        roleRepository.save(Role.buildInstance(role, userDetails));

        return mapTouserInfoDto(userDetails);
    }

    private boolean isViolatedRolesConstraints(String role, List<String> userRoles) {
        List<String> administriveRoles = List.of("ROLE_ADMINISTRATOR");
        List<String> businessRoles = List.of("ROLE_ACCOUNTANT", "ROLE_USER");

        if (administriveRoles.contains(role)) {
            for (var useRole : userRoles) {
                if (businessRoles.contains(useRole)) {
                    return true;
                }
            }
        }
        if (businessRoles.contains(role)) {
            for (var useRole : userRoles) {
                if (administriveRoles.contains(useRole)) {
                    return true;
                }
            }
        }
        return false;
    }

    public UserInfoDto removeRole(String user, String role) {

        if (role.equals("ROLE_ADMINISTRATOR")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Can't remove ADMINISTRATOR role!");
        }

        var userDetails = userEntityService
                .findUserDetailsEntityByEmail(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't update roles, user doesn't exist"));

        var roles = roleRepository.findAllByUserDetailsEntity(userDetails);

        Role roleToRemove = null;
        for (var r : roles) {
            if (r.getAuthority().equals(role)) {
                roleToRemove = r;
                break;
            }
        }

        if (roleToRemove == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The user does not have a role!");
        } else if (roles.size() == 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The user must have at least one role!");
        } else {
            roleRepository.delete(roleToRemove);
            return mapTouserInfoDto(userDetails);
        }
//        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "couldn't update remove user roles");
    }

    @Transactional
    public List<UserInfoDto> getAllUsersInfo() {
        return  userEntityService
                .findAll()
                .stream().map(this::mapTouserInfoDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public void removeUser(String email) {

        var user = userEntityService
                .findUserDetailsEntityByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found!"));
        var roles = roleRepository.findAllByUserDetailsEntity(user);

        if (roles.stream()
                .filter(role -> role.getAuthority().equals("ROLE_ADMINISTRATOR"))
                .collect(Collectors.toList())
                .size() == 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Can't remove ADMINISTRATOR role!");
        }

        roleRepository.deleteAll(roles);
        userEntityService.removeUser(email);
    }

    private UserInfoDto mapTouserInfoDto(UserDetailsEntity userDetailsEntity) {
        UserInfoDto userInfoDto = UserInfoDto.builder()
                .id(userDetailsEntity.getId())
                .name(userDetailsEntity.getName())
                .lastname(userDetailsEntity.getLastname())
                .email(userDetailsEntity.getEmail())
                .roles(roleRepository
                        .findAllByUserDetailsEntity(userDetailsEntity)
                        .stream()
                        .map(Role::getAuthority)
                        .collect(Collectors.toList()))
                .build();
        Collections.sort(userInfoDto.getRoles());
        return userInfoDto;
    }

    //TODO: save roles in seperate table and validate from it
    private void checkRoleName(String role) {
        var validRoles = List.of("ROLE_USER", "ROLE_ACCOUNTANT", "ROLE_ADMINISTRATOR");
        if (!validRoles.contains(role)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!");
        }
    }
}
