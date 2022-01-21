package account.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ErrorDto {
    private Date timestamp;
    private int status;
    private String error;
    private String path;
    private String message;
}
