package account.admin.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserInfoDto {
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private List<String> roles;
}
