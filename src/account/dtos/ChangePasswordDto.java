package account.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ChangePasswordDto {
    @NotBlank(message = "Password length must be 12 chars minimum!")
    @Size(min = 12, message = "Password length must be 12 chars minimum!")
    private String new_password;
}
