package account.security.userdetails;

import account.dtos.UserDto;
import account.security.userdetails.repository.UserDetails;

public class UserMapper {
    public static UserDto mapToUserDto(UserDetails user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .lastname(user.getLastname())
                .build();
    }

    public static UserDetails mapToUserDetails(UserDto dto) {
        var userDetails = new UserDetails();
        userDetails.setEmail(new String(dto.getEmail()));
        userDetails.setLastname(dto.getLastname());
        userDetails.setName(dto.getName());
        userDetails.setId(null);
        userDetails.setEmail(userDetails.getEmail());
        return userDetails;
    }
}
