package account.admin.dtos;

import lombok.Data;

@Data
public class DeleteUserResponseDto {
    private String user;
    private String status;

    public DeleteUserResponseDto(String user, String status) {
        this.user = user;
        this.status = status;
    }
}
