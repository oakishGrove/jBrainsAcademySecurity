package account.admin.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeAccessDto {
    private String user;
    private String operation;
}
