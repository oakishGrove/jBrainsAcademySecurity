package account.admin.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeRoleDto {
    private String user;
    private String role;
    private String operation;
}
