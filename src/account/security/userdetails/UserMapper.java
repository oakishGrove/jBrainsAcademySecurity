package account.security.userdetails;

import account.dtos.UserDto;
import account.security.userdetails.repository.Role;
import account.security.userdetails.repository.UserDetailsEntity;
import org.springframework.context.annotation.Profile;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto mapToUserDto(UserDetailsEntity user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .lastname(user.getLastname())
                .roles(user.getRoles().stream()
                        .map(Role::getAuthority)
                        .collect(Collectors.toList()))
                .build();
    }

    public static UserDetailsEntity mapToUserDetails(UserDto dto) {
        var userDetails = new UserDetailsEntity();
        userDetails.setEmail(new String(dto.getEmail()));
        userDetails.setLastname(dto.getLastname());
        userDetails.setName(dto.getName());
        userDetails.setId(null);
        userDetails.setEmail(userDetails.getEmail());
        return userDetails;
    }
}
