package account.dtos;

import lombok.Data;

@Data
public class SignUpDto {
    private String name;
    private String lastname;
    private String email;
    private String password;
}
