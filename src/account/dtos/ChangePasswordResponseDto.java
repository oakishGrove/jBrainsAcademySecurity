package account.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordResponseDto {
    private String email;
    private String status;
}
