package account.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SignUpDto {
    @NotBlank
    private String name;
    @NotBlank
    private String lastname;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
