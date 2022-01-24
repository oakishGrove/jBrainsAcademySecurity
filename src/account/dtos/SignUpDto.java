package account.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class SignUpDto {
    @NotBlank(message = "name should be not empty")
    private String name;
    @NotBlank(message = "lastname should be not empty")
    private String lastname;
    @NotBlank(message = "email should be not empty")
    private String email;
    @NotBlank(message = "password should be not empty")
    private String password;
    @Size(min = 1, message = "roles should be not empty")
    List<String> roles;
}
