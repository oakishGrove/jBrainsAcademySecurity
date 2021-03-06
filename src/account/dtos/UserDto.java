package account.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private List<String> roles;
}
