package account.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import java.time.LocalDate;

@Data
@Builder
public class PayRollDto {
    private String employee;
    @JsonFormat(pattern = "mm-YYYY")
    private LocalDate period;
    @Min(value = 0)
    private Long salary;
}
