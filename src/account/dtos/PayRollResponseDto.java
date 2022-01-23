package account.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PayRollResponseDto {
    private String name;
    private String lastname;
    private LocalDate period;
    private String salary;
}
