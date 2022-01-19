package account.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpResponseDto {
    private String name;
    private String lastname;
    private String email;
}
